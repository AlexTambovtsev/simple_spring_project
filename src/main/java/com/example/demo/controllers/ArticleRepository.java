package com.example.demo.controllers;

/**
 * Created by Tambovtsev on 21.06.2017.
 */
public class ArticleRepository {
    private SiteParam[] params;
    {
        fillArticles();
    }

    Integer numberOfArticles=params.length;

    public String getTitleOfArticle(int articleNumber) {
        return params[articleNumber-1].title;
    }

    public void editArticle(SiteParam param, Integer articleNumber) {
        params[articleNumber]=param;
    }

    public SiteParam getArticle(int articleNumber) {
        return params[articleNumber-1];
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
}
