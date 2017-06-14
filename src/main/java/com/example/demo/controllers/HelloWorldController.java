package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

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

    public void addArticle(SiteParam article) {
        SiteParam[] newParams = new SiteParam[params.length+1];
        for (int i=0; i<params.length; i++) {
            newParams[i]=params[i];
        }
        newParams[newParams.length-1]=article;
        params=newParams;
    }

    SiteParam[] params;
    public Integer getRealArticleNumber(Integer page, String adress) {
        if (page==null || page<1) {
            page=1;
        }
        if (adress == "page" && page>params.length) {
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
        int numberOfLinks=9;
        int prev = numberOfLinks/2;
        int next = numberOfLinks-prev-1;
        int maxPagesOfTitels = params.length/3+1;
        if (params.length%3==0) {
            maxPagesOfTitels--;
        }
        if (page > 1) {
            minus = "<a href=http://localhost:8080/site/" + adress + "?page=" + (page-1) + ">" + "prev" + "</a> ";
        }
        if (adress == "page" && page < params.length) {
            plus = " <a href=http://localhost:8080/site/" + adress + "?page=" + (page+1) + ">" + "next" + "</a>" + "<br>";
        }
        if (adress == "title" && page < maxPagesOfTitels) {
            plus = " <a href=http://localhost:8080/site/" + adress + "?page=" + (page+1) + ">" + "next" + "</a>" + "<br>";
        }
        if (adress == "page" && params.length <= numberOfLinks) {
            for (int i = 1; i <= params.length; i++) {
                if (i != page) {
                    numPage += "<a href=http://localhost:8080/site/" + adress + "?page=" + i + ">" + i + "</a>" + " ";
                } else {
                    numPage += "[" + String.valueOf(i) + "]" + " ";
                }
            }
        }
        else if (adress == "title" && params.length <= numberOfLinks*3) {
            for  (int i=1; i<=maxPagesOfTitels; i++) {
                if (i != page) {
                    numPage += "<a href=http://localhost:8080/site/" + adress + "?page=" + i + ">" + i + "</a>" + " ";
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
            if (adress == "page" && page >= (params.length-next)) {
                prev = page - params.length + numberOfLinks-1;
                next = params.length - page;
            }
            if (adress == "page") {
                for (int i = page-prev; i <= page+next; i++) {
                    if (i != page) {
                        numPage += "<a href=http://localhost:8080/site/" + adress + "?page=" + i + ">" + i + "</a>" + " ";
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
                        numPage += "<a href=http://localhost:8080/site/" + adress + "?page=" + i + ">" + i + "</a>" + " ";
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
            titles += "<a href=http://localhost:8080/site/view?articleNumber=" + i + ">" + params[i-1].title + "</a>" + "<br>";
            i++;
        }
            return titles + "<br></body>";
    }

    @GetMapping("/page")
    @ResponseBody
    public String getPage(Integer page) {
        page=getRealArticleNumber(page, "page");
        String numPage = "<body><br>" + getLinksToArticles(page, "page");
        String viewPage="<a href=http://localhost:8080/site/view?articleNumber=" + page +">" + "view_" + page + "</a>"+" ";
        return numPage + "<br>" +
                "<h3><b>" + params[page-1].title + "</h3></b><br>" +
                params[page-1].text + "<br>" +
                viewPage + "<br></body>";
    }

    @GetMapping("/view")
    @ResponseBody
    public String getView(Integer articleNumber) {
        articleNumber=getRealArticleNumber(articleNumber, "page");
        return "<body><br>" +
                "<form method='post'>" +
                "<h3><b>" +
                "<input type='text' name='title' placeholder =" + params[articleNumber-1].title + ">" + "</input>" + "</h3></b><br>" +
                "<br>" +
                "<textarea name='text'>" + params[articleNumber-1].text + "</textarea>" + "<br>" +
                "<input type='submit'/>" +
                "<input type='submit' name='delete' value='delete'/>" + "<br>" +
                "</form>" +
                "<br></body>";
    }

    @PostMapping("/view")
    @ResponseBody
    public String postViewOrDelArticle(String text, String title, Integer articleNumber, String delete) {
        articleNumber=getRealArticleNumber(articleNumber, "page");
        if (delete!=null) {
            SiteParam[] newParams=new SiteParam[params.length-1];
            for (int i=0; i<articleNumber-1; i++) {
                newParams[i]=params[i];
            }
            for (int i=articleNumber-1; i<newParams.length; i++) {
                newParams[i]=params[i+1];
            }
            params=newParams;
            return getView(articleNumber);
        }
        params[articleNumber-1].title=title;
        params[articleNumber-1].text=text;
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
    public String postNewArticle(String title, String text) {
        SiteParam param = new SiteParam();
        param.title=title;
        param.text=text;
        addArticle(param);
        return "Done";
    }
}

