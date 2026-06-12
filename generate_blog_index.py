#!/usr/bin/env python3
"""
Generate blog-index.json from Markdown files with YAML front matter.
"""

import json
import re
import yaml
from pathlib import Path
from datetime import datetime
from typing import Dict, List, Optional, Any
from urllib.parse import quote

REPO_ROOT = Path("/Users/vishnusimhadussa/Documents/GitHub/Intellij-workspace/Blogs")
NOTES_DIR = REPO_ROOT / "Notes"
RAW_BASE_URL = "https://raw.githubusercontent.com/Vishnusimha/Blogs/main"
EXCLUDE_PATTERNS = ["Archive", "archive", "deprecated", "backup", "old", "draft"]
WORDS_PER_MINUTE = 200
SUPPORTED_NOTE_EXTENSIONS = {
    ".md",
    ".mdx",
    ".java",
    ".txt",
    ".json",
    ".xml",
    ".kt",
    ".js",
    ".css",
    ".html",
}
LANGUAGE_BY_EXTENSION = {
    ".md": "markdown",
    ".mdx": "markdown",
    ".java": "java",
    ".kt": "kotlin",
    ".js": "javascript",
    ".json": "json",
    ".xml": "xml",
    ".css": "css",
    ".html": "html",
    ".txt": "text",
}
GENERATED_FILENAMES = {"blog-index.json"}

def should_exclude(path: Path) -> bool:
    """Check if path should be excluded."""
    parts = path.parts
    for part in parts:
        for pattern in EXCLUDE_PATTERNS:
            if pattern.lower() in part.lower():
                return True
    return False

def is_notes_path(path: Path) -> bool:
    """Check if path is inside the Notes directory."""
    try:
        path.relative_to(NOTES_DIR)
        return True
    except ValueError:
        return False

def has_hidden_part(path: Path) -> bool:
    """Check whether any relative path part is hidden."""
    return any(part.startswith(".") for part in path.parts)

def extract_front_matter(content: str) -> Optional[Dict[str, Any]]:
    """Extract YAML front matter from markdown file."""
    if not content.startswith("---"):
        return None
    
    try:
        match = re.match(r"^---\n(.*?)\n---", content, re.DOTALL)
        if not match:
            return None
        
        front_matter_str = match.group(1)
        front_matter = yaml.safe_load(front_matter_str)
        return front_matter if isinstance(front_matter, dict) else None
    except Exception:
        return None

def get_first_paragraph(content: str) -> str:
    """Extract first paragraph from markdown content, skipping front matter."""
    try:
        match = re.search(r"^---\n.*?\n---\n+(.*?)(?:\n\n|\n#{1,6}\s)", content, re.DOTALL)
        if match:
            text = match.group(1).strip()
        else:
            text = content.split("---")[-1].strip()
        
        # Remove markdown syntax
        text = re.sub(r'[#*`_~\[\]()]', '', text)
        text = re.sub(r'\n+', ' ', text)
        text = re.sub(r'\s+', ' ', text).strip()
        
        # Truncate to reasonable summary length
        if len(text) > 200:
            text = text[:200] + "..."
        
        return text
    except Exception:
        return ""

def generate_slug_from_title(title: str) -> str:
    """Generate slug from title if not provided."""
    slug = title.lower()
    slug = re.sub(r'[^\w\s-]', '', slug)
    slug = re.sub(r'[\s_]+', '-', slug)
    slug = re.sub(r'-+', '-', slug)
    return slug.strip('-')

def generate_note_slug(rel_path: str) -> str:
    """Generate a stable Notes slug from the full relative path."""
    normalized = Path(rel_path).with_suffix("").as_posix()
    extension = Path(rel_path).suffix.lower().lstrip(".")
    slug_source = f"{normalized}-{extension}" if extension else normalized
    slug_source = re.sub(r'(?<=[A-Z])(?=[A-Z][a-z])', '-', slug_source)
    slug_source = re.sub(r'(?<=[a-z0-9])(?=[A-Z])', '-', slug_source)
    slug = slug_source.lower()
    slug = re.sub(r'[\s_/.-]+', '-', slug)
    slug = re.sub(r'[^\w-]', '', slug)
    slug = re.sub(r'-+', '-', slug)
    return slug.strip('-')

def humanize_note_title(filepath: Path) -> str:
    """Generate a readable title from a Notes filename."""
    extension = filepath.suffix.lower()
    if extension not in {".md", ".mdx", ".txt"}:
        return filepath.name

    title = filepath.stem
    title = re.sub(r"[_-]+", " ", title)
    title = re.sub(r"\s+", " ", title).strip()
    words = []
    for word in title.split(" "):
        if word.isupper() or any(char.isdigit() for char in word):
            words.append(word)
        else:
            words.append(word.capitalize())
    return " ".join(words) if words else filepath.name

def get_note_type(extension: str) -> str:
    """Map a Notes extension to the frontend content type."""
    if extension in {".md", ".mdx"}:
        return "markdown"
    if extension == ".txt":
        return "text"
    return "code"

def build_raw_url(rel_path: str) -> str:
    """Build a raw GitHub URL with path-safe URL encoding."""
    return f"{RAW_BASE_URL}/{quote(rel_path, safe='/')}"

def estimate_read_time(content: str) -> int:
    """Estimate read time from word count."""
    words = len(content.split())
    return max(1, round(words / WORDS_PER_MINUTE))

def normalize_date(date: Any) -> str:
    """Normalize supported front matter date values."""
    if isinstance(date, str):
        return date
    return datetime.strftime(date, "%Y-%m-%d") if date else ""

def process_markdown_files() -> tuple[List[Dict], List[str], List[str]]:
    """Process all markdown files and extract metadata."""
    entries = []
    skipped = []
    errors = []
    
    # Find all .md files
    md_files = list(REPO_ROOT.rglob("*.md"))
    
    for filepath in sorted(md_files):
        # Skip excluded paths
        if should_exclude(filepath):
            skipped.append(str(filepath.relative_to(REPO_ROOT)))
            continue

        # Notes are indexed by the dedicated Notes scanner.
        if is_notes_path(filepath):
            continue
        
        # Skip README and report files
        if filepath.name in ["README.md", "FRONT_MATTER_UPDATE_REPORT.md"]:
            skipped.append(str(filepath.relative_to(REPO_ROOT)))
            continue
        
        try:
            with open(filepath, 'r', encoding='utf-8') as f:
                content = f.read()
            
            front_matter = extract_front_matter(content)
            if not front_matter:
                skipped.append(str(filepath.relative_to(REPO_ROOT)))
                continue
            
            # Extract fields
            title = front_matter.get("title", "").strip()
            date = front_matter.get("date")
            slug = front_matter.get("slug", "").strip()
            summary = front_matter.get("summary", "").strip()
            categories = front_matter.get("categories")
            read_time = front_matter.get("readTime")
            
            # Validate/generate required fields
            if not title:
                errors.append(f"{filepath.relative_to(REPO_ROOT)}: Missing title")
                continue
            
            if not slug:
                slug = generate_slug_from_title(title)
            
            if not summary:
                summary = get_first_paragraph(content)
                if not summary:
                    summary = title
            
            if not read_time:
                read_time = estimate_read_time(content)
            
            # Normalize date
            date_str = normalize_date(date)
            
            if not date_str:
                errors.append(f"{filepath.relative_to(REPO_ROOT)}: Missing date")
                continue
            
            # Get relative path
            rel_path = str(filepath.relative_to(REPO_ROOT))
            
            # Create entry
            entry = {
                "title": title,
                "date": date_str,
                "slug": slug,
                "summary": summary,
                "categories": categories if categories else "General",
                "readTime": int(read_time),
                "path": rel_path
            }
            
            entries.append(entry)
        
        except Exception as e:
            errors.append(f"{filepath.relative_to(REPO_ROOT)}: {str(e)}")
    
    return entries, skipped, errors

def process_notes_files() -> tuple[List[Dict], List[str], List[str]]:
    """Process supported files under Notes and generate note metadata."""
    entries = []
    skipped = []
    errors = []

    if not NOTES_DIR.exists():
        return entries, skipped, errors

    for filepath in sorted(NOTES_DIR.rglob("*"), key=lambda path: path.as_posix()):
        if not filepath.is_file():
            continue

        rel_path = str(filepath.relative_to(REPO_ROOT))
        rel_notes_path = filepath.relative_to(NOTES_DIR)
        extension = filepath.suffix.lower()

        if has_hidden_part(rel_notes_path) or filepath.name == ".DS_Store":
            skipped.append(rel_path)
            continue

        if filepath.name in GENERATED_FILENAMES:
            skipped.append(rel_path)
            continue

        if extension not in SUPPORTED_NOTE_EXTENSIONS:
            skipped.append(rel_path)
            continue

        try:
            entry = {
                "title": humanize_note_title(filepath),
                "slug": generate_note_slug(rel_path),
                "category": "Notes",
                "categories": "Notes",
                "type": get_note_type(extension),
                "extension": extension,
                "language": LANGUAGE_BY_EXTENSION[extension],
                "path": rel_path,
                "rawUrl": build_raw_url(rel_path),
                "isNote": True,
            }

            if extension in {".md", ".mdx"}:
                with open(filepath, 'r', encoding='utf-8') as f:
                    content = f.read()

                front_matter = extract_front_matter(content)
                if front_matter:
                    title = str(front_matter.get("title") or "").strip()
                    summary = str(front_matter.get("summary") or "").strip()
                    date_str = normalize_date(front_matter.get("date"))
                    read_time = front_matter.get("readTime")

                    if title:
                        entry["title"] = title
                    if summary:
                        entry["summary"] = summary
                    if date_str:
                        entry["date"] = date_str
                    if read_time:
                        entry["readTime"] = int(read_time)

            entries.append(entry)
        except Exception as e:
            errors.append(f"{rel_path}: {str(e)}")

    return entries, skipped, errors

def validate_entries(entries: List[Dict]) -> tuple[bool, List[str]]:
    """Validate entries for duplicates and required fields."""
    issues = []
    
    # Check for duplicate slugs
    slugs = [e["slug"] for e in entries]
    duplicates = [s for s in set(slugs) if slugs.count(s) > 1]
    if duplicates:
        issues.append(f"Duplicate slugs found: {duplicates}")
    
    # Check for missing required fields
    for entry in entries:
        if not entry.get("title"):
            issues.append(f"Entry missing title: {entry.get('path')}")
        if not entry.get("slug"):
            issues.append(f"Entry missing slug: {entry.get('path')}")
        if not entry.get("path"):
            issues.append("Entry missing path")
    
    return len(issues) == 0, issues

def main():
    """Main function."""
    print("Scanning markdown files...")
    post_entries, post_skipped, post_errors = process_markdown_files()
    print("Scanning Notes files...")
    note_entries, note_skipped, note_errors = process_notes_files()
    
    # Preserve normal post sorting, then append Notes in deterministic path order.
    post_entries.sort(key=lambda x: (-datetime.strptime(x["date"], "%Y-%m-%d").timestamp(), x["title"]))
    note_entries.sort(key=lambda x: x["path"])
    entries = post_entries + note_entries
    skipped = post_skipped + note_skipped
    errors = post_errors + note_errors
    
    # Validate
    is_valid, validation_issues = validate_entries(entries)
    
    # Write to file
    output_path = REPO_ROOT / "blog-index.json"
    with open(output_path, 'w', encoding='utf-8') as f:
        json.dump(entries, f, indent=2, ensure_ascii=False)
    
    # Print report
    print("\n" + "="*60)
    print("BLOG INDEX GENERATION REPORT")
    print("="*60)
    print(f"\n✓ Total posts included: {len(post_entries)}")
    print(f"✓ Total Notes included: {len(note_entries)}")
    print(f"✓ Total entries included: {len(entries)}")
    print(f"✓ Total files skipped: {len(skipped)}")
    print(f"✓ Errors encountered: {len(errors)}")
    print(f"✓ Output file: {output_path}")
    
    if validation_issues:
        print(f"\n⚠ Validation issues ({len(validation_issues)}):")
        for issue in validation_issues:
            print(f"  - {issue}")
    else:
        print("\n✓ All validation checks passed!")
    
    if errors:
        print(f"\n⚠ Processing errors ({len(errors)}):")
        for error in errors[:10]:  # Show first 10
            print(f"  - {error}")
        if len(errors) > 10:
            print(f"  ... and {len(errors) - 10} more")
    
    if skipped:
        print(f"\n ℹ Skipped files ({len(skipped)}):")
        for skip in skipped[:10]:  # Show first 10
            print(f"  - {skip}")
        if len(skipped) > 10:
            print(f"  ... and {len(skipped) - 10} more")
    
    print(f"\n✓ JSON is valid: {is_valid}")
    
    # Verify paths exist
    path_issues = []
    for entry in entries:
        full_path = REPO_ROOT / entry["path"]
        if not full_path.exists():
            path_issues.append(entry["path"])
    
    if path_issues:
        print(f"\n⚠ Path verification issues: {len(path_issues)}")
        for path in path_issues:
            print(f"  - {path}")
    else:
        print("✓ All paths verified to exist")
    
    print("\n" + "="*60)
    
    return True

if __name__ == "__main__":
    main()
