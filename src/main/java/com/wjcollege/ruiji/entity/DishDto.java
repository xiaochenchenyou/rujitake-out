package com.wjcollege.ruiji.entity;

import com.wjcollege.ruiji.entity.Dish;
import com.wjcollege.ruiji.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
