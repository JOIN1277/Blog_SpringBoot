package com.klx.web;

import com.klx.NotFoundException;
import com.klx.service.BlogService;
import com.klx.service.TagService;
import com.klx.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @GetMapping("/")
    public String index(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model) {
        model.addAttribute("page",blogService.listBlog(pageable));//拿到文章分页数据
        model.addAttribute("types", typeService.listTypeTop(6));//拿到分类分页数据，取前6个
        model.addAttribute("tags", tagService.listTagTop(10));//拿到标签分页数据 ，取前10个
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(8));
        return "index";
    }
    @PostMapping("/search")
    public String search(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query, Model model) {
        model.addAttribute("page", blogService.listBlog("%"+query+"%", pageable));
        model.addAttribute("query", query);
        return "search";
    }
    @GetMapping("/blog/{id}")//根据id查询博客
   public String blog(@PathVariable Long id,Model model) { //内容放入model
        model.addAttribute("blog", blogService.getAndConvert(id)); //通过博客id主键拿到博客对象
        return "blog";
    }
    @GetMapping("/footer/newblog")
    public String newblogs(Model model) {
        model.addAttribute("newblogs", blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }
}
