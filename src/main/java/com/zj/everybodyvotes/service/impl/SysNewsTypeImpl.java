package com.zj.everybodyvotes.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zj.everybodyvotes.dao.INewsTypeDao;
import com.zj.everybodyvotes.domain.SysNewsType;
import com.zj.everybodyvotes.service.ISysNewsTypeService;
import org.springframework.stereotype.Service;

/**
 * @author cuberxp
 * @date 2021/5/15 8:08 下午
 */
@Service
public class SysNewsTypeImpl extends ServiceImpl<INewsTypeDao, SysNewsType> implements ISysNewsTypeService {
}
