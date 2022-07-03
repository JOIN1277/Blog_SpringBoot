package com.klx.web.admin;
import com.klx.po.Blog;
import com.klx.po.User;
import com.klx.service.BlogService;
import com.klx.service.TagService;
import com.klx.service.TypeService;
import com.klx.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")  //amdin目录下
public class BlogController {
    private static final String INPUT = "admin/blogs-input";   //文章新增页面
    private static final String LIST = "admin/blogs";          //文章列表页面
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";   //重定向至文章列表
    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")  //文章列表页面
    //指定页数大小 根据更新时间排序
    public String blogs(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog, Model model) {
        model.addAttribute("types", typeService.listType());  //添加类型至model，供前端展示 使用typeService接口下的方法
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return LIST;
    }
    @PostMapping("/blogs/search") //文章搜索页面
    public String search(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogs :: blogList";
    }
    @GetMapping("/blogs/input") //文章新增页面
    public String input(Model model) {
        setTypeAndTag(model);
        model.addAttribute("blog", new Blog());  //添加一个新文章
        return INPUT;
    }
    private void setTypeAndTag(Model model) { //设置类型和标签
        model.addAttribute("types", typeService.listType()); //添加一个类型
        model.addAttribute("tags", tagService.listTag());    //添加标签
    }
    @GetMapping("/blogs/{id}/input")   //文章编辑页面
    public String editInput(@PathVariable Long id, Model model) {
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        return INPUT;
    }
    @PostMapping("/blogs")   //
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session) {
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog b;
        if (blog.getId() == null) {
            b =  blogService.saveBlog(blog);
        } else {
            b = blogService.updateBlog(blog.getId(), blog);
        }

        if (b == null ) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return REDIRECT_LIST;
    }
    @GetMapping("/blogs/{id}/delete") //删除一个文章
    public String delete(@PathVariable Long id,RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return REDIRECT_LIST;
    }

}
