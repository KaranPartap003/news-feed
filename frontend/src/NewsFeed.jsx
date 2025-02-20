import {React, useEffect, useState} from 'react'
import NewsCard from './NewsCard';

function NewsFeed() {
    const [pageNumber, setPage] = useState(0);
    const [articles, setArticles] = useState([]);
    const [hasMore, setHasMore] = useState(true);
    const [interactedArticles, setInteractedArticles] = useState([]);
    const pageSize = 5;

    //function to fetch articles from api
    const fetchArticles = async (currentPage) => {
        const payload = (interactedArticles.length !== 0 )
                ? JSON.stringify({ userArticles: interactedArticles }) 
                : null;
    
        try{
            const response = await fetch(
                `http://localhost:8080/api/news-feed/getUserArticles?pageNo=${currentPage}&pageSize=${pageSize}`,
                {
                    method: 'POST',
                    body: payload,
                    headers:{
                        'Content-Type': 'application/json'
                    }
                }
            );
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
    
            const jsonData = await response.json();
            
            if(payload !== null)
                console.log("payload : ", payload);

            if(jsonData.length === 0) {
                setHasMore(false);
            }
            else{
                setArticles((prevArticles) => [...prevArticles, ...jsonData]);
            }
          } catch (error) {
            console.error("Error fetching articles:", error);
          }
    };


    useEffect(() => {
        console.log(pageNumber);
        fetchArticles(pageNumber);
        setInteractedArticles([]);
    },[pageNumber]
    );

    //to check if user scrolled to bottom of page
    const handleScroll = () =>{
        if (
            window.innerHeight + document.documentElement.scrollTop + 1 >= document.documentElement.offsetHeight &&
            hasMore
          ) {
            setPage(prevPage => prevPage + 1);
          }
    }

    //add scroll event listener to scroll component when it mounts
    useEffect( () => {
        window.addEventListener('scroll', handleScroll);
        return () => window.removeEventListener('scroll', handleScroll);
    }, [hasMore]
    );


    //handles user interaction 
    const handleInteraction = (articleTitle) =>{
        setInteractedArticles(
            prevArticles => {
                if(prevArticles.includes(articleTitle))             //if article already exists, simply return it
                    return prevArticles;
                if(prevArticles.length < 3)                         //if count is less than 3, simply add one
                    return [...prevArticles, articleTitle];
                return [...prevArticles.slice(1), articleTitle];    //remove last added article and add current
            }
        )
    }

    return(
        <div>
            {
                articles.map( (responseArticle, index) => 
                    <NewsCard
                        key={index}
                        article={responseArticle}
                        onInteract={handleInteraction}
                    />
                )
            }
            {!hasMore && <p>No more articles to load</p>}
        </div>
    )

}

export default NewsFeed;