package com.example.thymeleaf.controller;

import com.example.thymeleaf.entity.Product;
import com.example.thymeleaf.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Display all products
     */
    @GetMapping
    public String listProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            Model model) {

        List<Product> products;

        if (search != null && !search.isEmpty()) {
            products = productService.searchByName(search);
            model.addAttribute("search", search);
        } else if (category != null && !category.isEmpty()) {
            products = productService.getProductsByCategory(category);
            model.addAttribute("selectedCategory", category);
        } else {
            products = productService.getAllProducts();
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("totalProducts", productService.countProducts());

        return "products/list";
    }

    /**
     * Show create product form
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("isNew", true);
        return "products/form";
    }

    /**
     * Create a new product
     */
    @PostMapping
    public String createProduct(
            @Valid @ModelAttribute("product") Product product,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("categories", productService.getAllCategories());
            model.addAttribute("isNew", true);
            return "products/form";
        }

        try {
            productService.createProduct(product);
            redirectAttributes.addFlashAttribute("successMessage", "Product created successfully!");
            return "redirect:/products";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("categories", productService.getAllCategories());
            model.addAttribute("isNew", true);
            return "products/form";
        }
    }

    /**
     * Show product details
     */
    @GetMapping("/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        Optional<Product> product = productService.getProductById(id);

        if (product.isEmpty()) {
            return "redirect:/products";
        }

        model.addAttribute("product", product.get());
        return "products/view";
    }

    /**
     * Show edit product form
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Product> product = productService.getProductById(id);

        if (product.isEmpty()) {
            return "redirect:/products";
        }

        model.addAttribute("product", product.get());
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("isNew", false);
        return "products/form";
    }

    /**
     * Update an existing product
     */
    @PostMapping("/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @Valid @ModelAttribute("product") Product product,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("categories", productService.getAllCategories());
            model.addAttribute("isNew", false);
            return "products/form";
        }

        try {
            productService.updateProduct(id, product);
            redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully!");
            return "redirect:/products/" + id;
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("categories", productService.getAllCategories());
            model.addAttribute("isNew", false);
            return "products/form";
        }
    }

    /**
     * Delete a product
     */
    @PostMapping("/{id}/delete")
    public String deleteProduct(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/products";
    }

    /**
     * Display home/dashboard page
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalProducts", productService.countProducts());
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("lowStockProducts", productService.getLowStockProducts(10));

        return "dashboard";
    }

    /**
     * Redirect root to products
     */
    @GetMapping("/")
    public String redirectToProducts() {
        return "redirect:/products";
    }
}
