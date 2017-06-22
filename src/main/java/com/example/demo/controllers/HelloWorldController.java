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

    ArticleRepository articleRepository=new ArticleRepository();

    public String getLinksToArticles(Integer page, String adress) {
        String minus = "prev ";
        String plus = " next<br>";
        String numPage = "";
        String isItPageOrArticleNumber;
        int numberOfLinks=9;
        int prev = numberOfLinks/2;
        int next = numberOfLinks-prev-1;
        int maxPagesOfTitels = articleRepository.numberOfArticles/3+1;
        if (adress=="page" || adress=="title") {
            isItPageOrArticleNumber="?page=";
        }
        else {
            isItPageOrArticleNumber="?articleNumber=";
        }
        if (articleRepository.numberOfArticles%3==0) {
            maxPagesOfTitels--;
        }
        if (page > 1) {
            minus = "<a href=http://localhost:8080/site/" + adress + isItPageOrArticleNumber + (page-1) + ">" + "prev" + "</a> ";
        }
        if ((adress=="page" || adress=="view") && page < articleRepository.numberOfArticles) {
            plus = " <a href=http://localhost:8080/site/" + adress + isItPageOrArticleNumber + (page+1) + ">" + "next" + "</a>" + "<br>";
        }
        if (adress=="title" && page < maxPagesOfTitels) {
            plus = " <a href=http://localhost:8080/site/" + adress + isItPageOrArticleNumber + (page+1) + ">" + "next" + "</a>" + "<br>";
        }
        if ((adress=="page" || adress=="view") && articleRepository.numberOfArticles <= numberOfLinks) {
            for (int i = 1; i <= articleRepository.numberOfArticles; i++) {
                if (i != page) {
                    numPage += "<a href=http://localhost:8080/site/" + adress + isItPageOrArticleNumber + i + ">" + i + "</a>" + " ";
                } else {
                    numPage += "[" + String.valueOf(i) + "]" + " ";
                }
            }
        }
        else if (adress == "title" && articleRepository.numberOfArticles <= numberOfLinks*3) {
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
            if ((adress=="page" || adress=="view") && page >= (articleRepository.numberOfArticles-next)) {
                prev = page - articleRepository.numberOfArticles + numberOfLinks-1;
                next = articleRepository.numberOfArticles - page;
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
            if (adress == "title" && page > (articleRepository.numberOfArticles/3-next)) {
                prev = page-articleRepository.numberOfArticles/3+numberOfLinks-1;
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
        page=articleRepository.getRealArticleNumber(page, "title");
        String titles="<body><br>" + getLinksToArticles(page, "title");
        int i=page*3-2;
        while (i<=articleRepository.numberOfArticles && i<=page*3) {
            titles += "<a href=http://localhost:8080/site/view?articleNumber=" + i + ">" + articleRepository.getTitleOfArticle(i) + "</a>" + "<br>";
            i++;
        }
            return titles + "<br></body>";
    }

    @GetMapping("/page")
    @ResponseBody
    public String getPage(Integer page) {
        page=articleRepository.getRealArticleNumber(page, "page");
        SiteParam article=articleRepository.getArticle(page);
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
        articleNumber=articleRepository.getRealArticleNumber(articleNumber, "view");
        SiteParam article=articleRepository.getArticle(articleNumber);
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
        articleNumber=articleRepository.getRealArticleNumber(articleNumber, "page");
        if (delete!=null) {
            articleRepository.removeArticle(articleNumber);
            return getView(articleNumber);
        }
        articleRepository.editArticle(param, articleNumber-1);
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
        articleRepository.addArticle(param);
        return "Done";
    }
}

