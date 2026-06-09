/**
 * Example React Hook for using blog-index.json
 * 
 * This is a sample implementation showing how to:
 * 1. Fetch the blog index
 * 2. Build blog cards with metadata
 * 3. Filter and paginate posts
 */

// ============================================================================
// 1. FETCH BLOG INDEX HOOK
// ============================================================================

import { useEffect, useState } from 'react';

export const useBlogIndex = () => {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchBlogIndex = async () => {
      try {
        const response = await fetch(
          'https://raw.githubusercontent.com/your-username/your-repo/main/blog-index.json'
        );
        if (!response.ok) throw new Error('Failed to fetch blog index');
        const data = await response.json();
        setPosts(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchBlogIndex();
  }, []);

  return { posts, loading, error };
};

// ============================================================================
// 2. BLOG CARD COMPONENT
// ============================================================================

const BlogCard = ({ post }) => {
  return (
    <article className="blog-card">
      <div className="card-header">
        <h3 className="card-title">{post.title}</h3>
        <time className="card-date" dateTime={post.date}>
          {new Date(post.date).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
          })}
        </time>
      </div>

      <p className="card-summary">{post.summary}</p>

      <div className="card-footer">
        <div className="card-meta">
          {Array.isArray(post.categories) ? (
            post.categories.map((cat, idx) => (
              <span key={idx} className="badge badge-category">
                {cat}
              </span>
            ))
          ) : (
            <span className="badge badge-category">{post.categories}</span>
          )}
        </div>
        <span className="read-time">
          📖 {post.readTime} {post.readTime === 1 ? 'min' : 'mins'}
        </span>
      </div>

      <a
        href={`/blog/${post.slug}`}
        className="card-link"
        aria-label={`Read: ${post.title}`}
      >
        Read Article →
      </a>
    </article>
  );
};

// ============================================================================
// 3. BLOG LIST COMPONENT WITH FILTERING & PAGINATION
// ============================================================================

const BlogList = () => {
  const { posts, loading, error } = useBlogIndex();
  const [filteredPosts, setFilteredPosts] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState('All');
  const [currentPage, setCurrentPage] = useState(1);
  const POSTS_PER_PAGE = 6;

  // Get all unique categories
  const categories = [
    'All',
    ...new Set(
      posts.flatMap((p) =>
        Array.isArray(p.categories) ? p.categories : [p.categories]
      )
    ),
  ];

  // Filter posts by category
  useEffect(() => {
    if (selectedCategory === 'All') {
      setFilteredPosts(posts);
    } else {
      setFilteredPosts(
        posts.filter((p) => {
          const cats = Array.isArray(p.categories) ? p.categories : [p.categories];
          return cats.includes(selectedCategory);
        })
      );
    }
    setCurrentPage(1);
  }, [posts, selectedCategory]);

  // Pagination
  const totalPages = Math.ceil(filteredPosts.length / POSTS_PER_PAGE);
  const startIdx = (currentPage - 1) * POSTS_PER_PAGE;
  const paginatedPosts = filteredPosts.slice(
    startIdx,
    startIdx + POSTS_PER_PAGE
  );

  if (loading) return <div className="loading">Loading blog posts...</div>;
  if (error) return <div className="error">Error: {error}</div>;

  return (
    <div className="blog-container">
      <h1>Blog Posts</h1>
      <p className="total-posts">Total: {filteredPosts.length} posts</p>

      {/* Category Filter */}
      <div className="category-filter">
        <label htmlFor="category-select">Filter by Category:</label>
        <select
          id="category-select"
          value={selectedCategory}
          onChange={(e) => setSelectedCategory(e.target.value)}
        >
          {categories.map((cat) => (
            <option key={cat} value={cat}>
              {cat}
            </option>
          ))}
        </select>
      </div>

      {/* Blog Grid */}
      <div className="blog-grid">
        {paginatedPosts.map((post) => (
          <BlogCard key={post.slug} post={post} />
        ))}
      </div>

      {/* Pagination Controls */}
      {totalPages > 1 && (
        <div className="pagination">
          <button
            onClick={() => setCurrentPage(Math.max(1, currentPage - 1))}
            disabled={currentPage === 1}
          >
            ← Previous
          </button>

          <div className="page-info">
            Page {currentPage} of {totalPages}
          </div>

          <button
            onClick={() => setCurrentPage(Math.min(totalPages, currentPage + 1))}
            disabled={currentPage === totalPages}
          >
            Next →
          </button>
        </div>
      )}
    </div>
  );
};

export default BlogList;

// ============================================================================
// 4. EXAMPLE CSS STYLES
// ============================================================================

const styles = `
.blog-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.category-filter {
  margin-bottom: 30px;
  display: flex;
  gap: 10px;
  align-items: center;
}

.category-filter select {
  padding: 8px 16px;
  border-radius: 4px;
  border: 1px solid #ddd;
  font-size: 16px;
}

.blog-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
  margin-bottom: 40px;
}

.blog-card {
  display: flex;
  flex-direction: column;
  padding: 24px;
  border: 1px solid #eee;
  border-radius: 8px;
  background: #fff;
  transition: all 0.3s ease;
  height: 100%;
}

.blog-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.card-title {
  margin: 0 0 12px 0;
  font-size: 18px;
  font-weight: 600;
  line-height: 1.4;
  color: #333;
}

.card-date {
  color: #666;
  font-size: 14px;
  margin-bottom: 16px;
}

.card-summary {
  flex-grow: 1;
  margin: 0 0 16px 0;
  color: #555;
  line-height: 1.6;
  font-size: 14px;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  gap: 12px;
  flex-wrap: wrap;
}

.card-meta {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 16px;
  font-size: 12px;
  font-weight: 500;
  background: #f0f0f0;
  color: #333;
}

.badge-category {
  background: #e8f4f8;
  color: #007acc;
}

.read-time {
  font-size: 12px;
  color: #666;
  white-space: nowrap;
}

.card-link {
  display: inline-block;
  color: #007acc;
  text-decoration: none;
  font-weight: 500;
  font-size: 14px;
  transition: color 0.2s;
}

.card-link:hover {
  color: #005a9c;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  margin-top: 40px;
}

.pagination button {
  padding: 8px 16px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background: #fff;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.pagination button:hover:not(:disabled) {
  background: #007acc;
  color: #fff;
  border-color: #007acc;
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  font-size: 14px;
  color: #666;
}

.loading,
.error {
  text-align: center;
  padding: 40px;
  font-size: 16px;
}

.error {
  color: #d32f2f;
  background: #ffebee;
  border-radius: 4px;
}

.total-posts {
  color: #666;
  font-size: 14px;
  margin-bottom: 20px;
}

@media (max-width: 768px) {
  .blog-grid {
    grid-template-columns: 1fr;
  }

  .card-footer {
    flex-direction: column;
    align-items: flex-start;
  }
}
`;

// ============================================================================
// 5. SEARCH FUNCTIONALITY (BONUS)
// ============================================================================

export const useSearchPosts = (posts, searchTerm) => {
  return posts.filter((post) =>
    [post.title, post.summary, post.categories]
      .flat()
      .join(' ')
      .toLowerCase()
      .includes(searchTerm.toLowerCase())
  );
};

// ============================================================================
// 6. GENERATE SITEMAP FROM BLOG INDEX
// ============================================================================

export const generateSitemap = (posts, baseUrl) => {
  const xml = `<?xml version="1.0" encoding="UTF-8"?>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
${posts
  .map(
    (post) => `
  <url>
    <loc>${baseUrl}/blog/${post.slug}</loc>
    <lastmod>${post.date}</lastmod>
    <changefreq>monthly</changefreq>
    <priority>0.8</priority>
  </url>`
  )
  .join('')}
</urlset>`;
  return xml;
};

// ============================================================================
// 7. GENERATE OG META TAGS
// ============================================================================

export const getPostMeta = (post, baseUrl) => ({
  title: post.title,
  description: post.summary,
  url: `${baseUrl}/blog/${post.slug}`,
  image: `${baseUrl}/og-images/${post.slug}.png`, // Optional: custom OG image
  author: 'Your Name',
  publishedDate: post.date,
  tags: Array.isArray(post.categories)
    ? post.categories.join(', ')
    : post.categories,
});
`;

console.log(styles); // Just for reference

export { BlogList, BlogCard };

