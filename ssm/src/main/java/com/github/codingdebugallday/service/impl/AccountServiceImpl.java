package com.github.codingdebugallday.service.impl;

import java.util.List;

import com.github.codingdebugallday.mapper.AccountMapper;
import com.github.codingdebugallday.pojo.Account;
import com.github.codingdebugallday.service.AccountService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/15 2:23
 * @since 1.0.0
 */
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountMapper accountMapper;

    public AccountServiceImpl(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Override
    public List<Account> queryAccountList() {
        return accountMapper.queryAccountList();
    }
}
