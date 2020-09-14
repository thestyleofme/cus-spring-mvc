package com.github.codingdebugallday.controller;

import java.util.List;

import com.github.codingdebugallday.pojo.Account;
import com.github.codingdebugallday.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/15 3:18
 * @since 1.0.0
 */
@Controller
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/list")
    @ResponseBody
    public List<Account> queryAll() {
        return accountService.queryAccountList();
    }

}
