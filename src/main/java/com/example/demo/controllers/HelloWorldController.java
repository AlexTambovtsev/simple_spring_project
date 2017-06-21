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

    ArticleRepository ArrOfArticles=new ArticleRepository();

    @GetMapping("/title")
    @ResponseBody
    public String getTitle(Integer page) {
        page=ArrOfArticles.getRealArticleNumber(page, "title");
        String titles="<body><br>" + ArrOfArticles.getLinksToArticles(page, "title");
        int i=page*3-2;
        while (i<=ArrOfArticles.numberOfArticles && i<=page*3) {
            titles += "<a href=http://localhost:8080/site/view?articleNumber=" + i + ">" + ArrOfArticles.getTitleOfArticle(i) + "</a>" + "<br>";
            i++;
        }
            return titles + "<br></body>";
    }

    @GetMapping("/page")
    @ResponseBody
    public String getPage(Integer page) {
        page=ArrOfArticles.getRealArticleNumber(page, "page");
        SiteParam article=ArrOfArticles.getArticle(page);
        String numPage = "<body><br>" + ArrOfArticles.getLinksToArticles(page, "page");
        String viewPage="<a href=http://localhost:8080/site/view?articleNumber=" + page +">" + "view_" + page + "</a>"+" ";
        return numPage + "<br>" +
                "<h3><b>" + article.title + "</h3></b><br>" +
                article.text + "<br>" +
                viewPage + "<br></body>";
    }

    @GetMapping("/view")
    @ResponseBody
    public String getView(Integer articleNumber) {
        articleNumber=ArrOfArticles.getRealArticleNumber(articleNumber, "view");
        SiteParam article=ArrOfArticles.getArticle(articleNumber);
        String numPage = "<body><br>" + ArrOfArticles.getLinksToArticles(articleNumber, "view");
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
        articleNumber=ArrOfArticles.getRealArticleNumber(articleNumber, "page");
        if (delete!=null) {
            ArrOfArticles.removeArticle(articleNumber);
            return getView(articleNumber);
        }
        ArrOfArticles.editArticle(param, articleNumber-1);
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
        ArrOfArticles.addArticle(param);
        return "Done";
    }
}

