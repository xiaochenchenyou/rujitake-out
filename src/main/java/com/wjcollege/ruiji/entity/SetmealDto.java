package com.wjcollege.ruiji.entity;

import com.wjcollege.ruiji.entity.Setmeal;
import com.wjcollege.ruiji.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
