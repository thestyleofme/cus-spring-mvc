package com.github.codingdebugallday.mapper;

import java.util.List;

import com.github.codingdebugallday.pojo.Account;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/15 2:09
 * @since 1.0.0
 */
public interface AccountMapper {

    /**
     * 查询所有账户
     *
     * @return List<Account>
     */
    List<Account> queryAccountList();
}
