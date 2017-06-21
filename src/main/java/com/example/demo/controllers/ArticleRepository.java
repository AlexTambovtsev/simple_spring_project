package com.example.demo.controllers;

/**
 * Created by Tambovtsev on 21.06.2017.
 */
public class ArticleRepository {
    private SiteParam[] params;
    {
        fillArticles();
    }

    public int numberOfArticles=params.length;

    public String getTitleOfArticle(int articleNumber) {
        return params[articleNumber-1].title;
    }

    public void editArticle(SiteParam param, Integer articleNumber) {
        params[articleNumber]=param;
    }

    public SiteParam getArticle(int articleNumber) {
        SiteParam article;
        article=params[articleNumber-1];
        return article;
    }

    public void addArticle(SiteParam article) {
        SiteParam[] newParams = new SiteParam[params.length+1];
        for (int i=0; i<params.length; i++) {
            newParams[i]=params[i];
        }
        newParams[newParams.length-1]=article;
        params=newParams;
    }

    public void removeArticle(int index) {
        SiteParam[] newParams=new SiteParam[params.length-1];
        for (int i=0; i<index-1; i++) {
            newParams[i]=params[i];
        }
        for (int i=index-1; i<newParams.length; i++) {
            newParams[i]=params[i+1];
        }
        params=newParams;
    }

    public Integer getRealArticleNumber(Integer page, String adress) {
        if (page==null || page<1) {
            page=1;
        }
        if ((adress == "page" || adress=="view") && page>params.length) {
            page=params.length;
        }
        if (adress == "title" && page>params.length/3) {
            if (params.length%3==0) {
                page=params.length/3;
            }
            else {
                page = params.length / 3 + 1;
            }
        }
        return page;
    }

    public void fillArticles() {
        if (params==null) {
            params = new SiteParam[42];
            for (int i = 0; i < params.length; i++) {
                params[i] = new SiteParam();
                params[i].title = "title_" + (i + 1);
                params[i].text = "text_" + (i + 1);
            }
        }
    }

    public String getLinksToArticles(Integer page, String adress) {
        String minus = "prev ";
        String plus = " next<br>";
        String numPage = "";
        String isItPageOrArticleNumber;
        int numberOfLinks=9;
        int prev = numberOfLinks/2;
        int next = numberOfLinks-prev-1;
        int maxPagesOfTitels = params.length/3+1;
        if (adress=="page" || adress=="title") {
            isItPageOrArticleNumber="?page=";
        }
        else {
            isItPageOrArticleNumber="?articleNumber=";
        }
        if (params.length%3==0) {
            maxPagesOfTitels--;
        }
        if (page > 1) {
            minus = "<a href=http://localhost:8080/site/" + adress + isItPageOrArticleNumber + (page-1) + ">" + "prev" + "</a> ";
        }
        if ((adress=="page" || adress=="view") && page < params.length) {
            plus = " <a href=http://localhost:8080/site/" + adress + isItPageOrArticleNumber + (page+1) + ">" + "next" + "</a>" + "<br>";
        }
        if (adress=="title" && page < maxPagesOfTitels) {
            plus = " <a href=http://localhost:8080/site/" + adress + isItPageOrArticleNumber + (page+1) + ">" + "next" + "</a>" + "<br>";
        }
        if ((adress=="page" || adress=="view") && params.length <= numberOfLinks) {
            for (int i = 1; i <= params.length; i++) {
                if (i != page) {
                    numPage += "<a href=http://localhost:8080/site/" + adress + isItPageOrArticleNumber + i + ">" + i + "</a>" + " ";
                } else {
                    numPage += "[" + String.valueOf(i) + "]" + " ";
                }
            }
        }
        else if (adress == "title" && params.length <= numberOfLinks*3) {
            for  (int i=1; i<=maxPagesOfTitels; i++) {
                if (i != page) {
                    numPage += "<a href=http://localhost:8080/site/" + adress + isItPageOrArticleNumber + i + ">" + i + "</a>" + " ";
                }
                else {
                    numPage += "[" + String.valueOf(i) + "]" + " ";
                }
            }
        }
        else {
            if (page < prev+1) {
                prev = page-1;
                next = numberOfLinks-page;
            }
            if ((adress=="page" || adress=="view") && page >= (params.length-next)) {
                prev = page - params.length + numberOfLinks-1;
                next = params.length - page;
            }
            if ((adress=="page" || adress=="view")) {
                for (int i = page-prev; i <= page+next; i++) {
                    if (i != page) {
                        numPage += "<a href=http://localhost:8080/site/" + adress + isItPageOrArticleNumber + i + ">" + i + "</a>" + " ";
                    }
                    else {
                        numPage += "[" + String.valueOf(i) + "]" + " ";
                    }
                }
            }
            if (adress == "title" && page > (params.length/3-next)) {
                prev = page-params.length/3+numberOfLinks-1;
                next = maxPagesOfTitels-page;
            }
            if (adress == "title") {
                for (int i = page-prev; i <= page+next; i++) {
                    if (i != page) {
                        numPage += "<a href=http://localhost:8080/site/" + adress + isItPageOrArticleNumber + i + ">" + i + "</a>" + " ";
                    }
                    else {
                        numPage += "[" + String.valueOf(i) + "]" + " ";
                    }
                }
            }
        }
        return minus + numPage + plus;
    }
}