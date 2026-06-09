#!/usr/bin/env python3
"""
Generate blog-index.json from Markdown files with YAML front matter.
"""

import os
import json
import re
import yaml
from pathlib import Path
from datetime import datetime
from typing import Dict, List, Optional, Any

REPO_ROOT = Path("/Users/vishnusimhadussa/Documents/GitHub/Intellij-workspace/Blogs")
EXCLUDE_PATTERNS = ["Archive", "archive", "deprecated", "backup", "old", "draft"]
WORDS_PER_MINUTE = 200

def should_exclude(path: Path) -> bool:
    """Check if path should be excluded."""
    parts = path.parts
    for part in parts:
        for pattern in EXCLUDE_PATTERNS:
            if pattern.lower() in part.lower():
                return True
    return False

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

def estimate_read_time(content: str) -> int:
    """Estimate read time from word count."""
    words = len(content.split())
    return max(1, round(words / WORDS_PER_MINUTE))

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
            if isinstance(date, str):
                date_str = date
            else:
                date_str = datetime.strftime(date, "%Y-%m-%d") if date else ""
            
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
    entries, skipped, errors = process_markdown_files()
    
    # Sort by date descending, then title ascending
    entries.sort(key=lambda x: (-datetime.strptime(x["date"], "%Y-%m-%d").timestamp(), x["title"]))
    
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
    print(f"\n✓ Total posts included: {len(entries)}")
    print(f"✓ Total posts skipped: {len(skipped)}")
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

