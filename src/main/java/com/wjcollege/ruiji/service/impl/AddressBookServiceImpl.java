package com.wjcollege.ruiji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjcollege.ruiji.entity.AddressBook;
import com.wjcollege.ruiji.mapper.AddressBookMapper;
import com.wjcollege.ruiji.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author chenWei
 * @date 2022/11/27 21:50
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
