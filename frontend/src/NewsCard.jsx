import React from 'react'

function NewsCard({article, onInteract}) {
  return (
    <div className={article.recommended ? 'recommended-article' : 'news-card'} 
        onClick={() => {onInteract(article.title)}}>
        <h3>{article.title}</h3>
        <p>{article.description}</p>
        <a href={article.link}
           onClick={(e) => {
            e.stopPropagation();
            onInteract(article.title);
           }}
           target='_blank'
        >
            Read more
        </a>
    </div>
  )
}

export default NewsCard