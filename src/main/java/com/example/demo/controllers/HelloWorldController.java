package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ISazonov on 28.04.2017.
 */
//Аннотация, которая помечает класс, как контроллер
@Controller
//Аннотация, которая говорит, что все методы класса будут генерировать странички.
//адрес которых начиченается с /site
@RequestMapping("/site")
public class HelloWorldController {
    {
        fillArticles();
    }

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

    SiteParam[] params;
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

    @GetMapping("/title")
    @ResponseBody
    public String getTitle(Integer page) {
        page=getRealArticleNumber(page, "title");
        String titles="<body><br>" + getLinksToArticles(page, "title");
        int i=page*3-2;
        while (i<=params.length && i<=page*3) {
            titles += "<a href=http://localhost:8080/site/view?articleNumber=" + i + ">" + getTitleOfArticle(i) + "</a>" + "<br>";
            i++;
        }
            return titles + "<br></body>";
    }

    @GetMapping("/page")
    @ResponseBody
    public String getPage(Integer page) {
        page=getRealArticleNumber(page, "page");
        SiteParam article=getArticle(page);
        String numPage = "<body><br>" + getLinksToArticles(page, "page");
        String viewPage="<a href=http://localhost:8080/site/view?articleNumber=" + page +">" + "view_" + page + "</a>"+" ";
        return numPage + "<br>" +
                "<h3><b>" + article.title + "</h3></b><br>" +
                article.text + "<br>" +
                viewPage + "<br></body>";
    }

    @GetMapping("/view")
    @ResponseBody
    public String getView(Integer articleNumber) {
        articleNumber=getRealArticleNumber(articleNumber, "view");
        SiteParam article=getArticle(articleNumber);
        String numPage = "<body><br>" + getLinksToArticles(articleNumber, "view");
        return numPage + "<br>" +
                "<form method='post'>" +
                "<h3><b>" +
                "<input type='text' name='title' placeholder =" + article.title + ">" + "</input>" + "</h3></b><br>" +
                "<br>" +
                "<textarea name='text'>" + article.text + "</textarea>" + "<br>" +
                "<input type='submit'/>" +
                "<input type='submit' name='delete' value='delete'/>" + "<br>" +
                "</form>" +
                "<br></body>";
    }

    @PostMapping("/view")
    @ResponseBody
    public String postViewOrDelArticle(SiteParam param, Integer articleNumber, String delete) {
        articleNumber=getRealArticleNumber(articleNumber, "page");
        if (delete!=null) {
            removeArticle(articleNumber);
            return getView(articleNumber);
        }
        editArticle(param, articleNumber-1);
        return getView(articleNumber);
    }

    @GetMapping("/newArticle")
    @ResponseBody
    public String getNewArticle() {
        SiteParam newArticle = new SiteParam();
        return "<body><br>" +
                "<form method='post'>" +
                "<h3><b>" +
                "<input type='text' name='title' placeholder =" + newArticle.title + ">" + "</input>" + "</h3></b><br>" +
                "<br>" +
                "<textarea name='text'>" + newArticle.text + "</textarea>" +
                "<input type='submit'/>" + "<br>" +
                "</form>" +
                "<br></body>";
    }

    @PostMapping("/newArticle")
    @ResponseBody
    public String postNewArticle(SiteParam param) {
        addArticle(param);
        return "Done";
    }
}

