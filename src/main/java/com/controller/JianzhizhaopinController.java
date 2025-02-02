package com.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;

import com.entity.JianzhizhaopinEntity;
import com.entity.view.JianzhizhaopinView;

import com.service.JianzhizhaopinService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.CommonUtil;


/**
 * 兼职招聘
 * 后端接口
 * @author 
 * @email 
 * @date 2020-12-02 15:06:32
 */
@RestController
@RequestMapping("/jianzhizhaopin")
public class JianzhizhaopinController {
    @Autowired
    private JianzhizhaopinService jianzhizhaopinService;
    


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,JianzhizhaopinEntity jianzhizhaopin, HttpServletRequest request){

		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("shangjia")) {
			jianzhizhaopin.setShangjiazhanghao((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<JianzhizhaopinEntity> ew = new EntityWrapper<JianzhizhaopinEntity>();
		PageUtils page = jianzhizhaopinService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jianzhizhaopin), params), params));
        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,JianzhizhaopinEntity jianzhizhaopin, HttpServletRequest request){
        EntityWrapper<JianzhizhaopinEntity> ew = new EntityWrapper<JianzhizhaopinEntity>();
		PageUtils page = jianzhizhaopinService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jianzhizhaopin), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( JianzhizhaopinEntity jianzhizhaopin){
       	EntityWrapper<JianzhizhaopinEntity> ew = new EntityWrapper<JianzhizhaopinEntity>();
      	ew.allEq(MPUtil.allEQMapPre( jianzhizhaopin, "jianzhizhaopin")); 
        return R.ok().put("data", jianzhizhaopinService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(JianzhizhaopinEntity jianzhizhaopin){
        EntityWrapper< JianzhizhaopinEntity> ew = new EntityWrapper< JianzhizhaopinEntity>();
 		ew.allEq(MPUtil.allEQMapPre( jianzhizhaopin, "jianzhizhaopin")); 
		JianzhizhaopinView jianzhizhaopinView =  jianzhizhaopinService.selectView(ew);
		return R.ok("查询兼职招聘成功").put("data", jianzhizhaopinView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
        JianzhizhaopinEntity jianzhizhaopin = jianzhizhaopinService.selectById(id);
        return R.ok().put("data", jianzhizhaopin);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") String id){
        JianzhizhaopinEntity jianzhizhaopin = jianzhizhaopinService.selectById(id);
        return R.ok().put("data", jianzhizhaopin);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody JianzhizhaopinEntity jianzhizhaopin, HttpServletRequest request){
    	jianzhizhaopin.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(jianzhizhaopin);

        jianzhizhaopinService.insert(jianzhizhaopin);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody JianzhizhaopinEntity jianzhizhaopin, HttpServletRequest request){
    	jianzhizhaopin.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(jianzhizhaopin);

        jianzhizhaopinService.insert(jianzhizhaopin);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody JianzhizhaopinEntity jianzhizhaopin, HttpServletRequest request){
        //ValidatorUtils.validateEntity(jianzhizhaopin);
        jianzhizhaopinService.updateById(jianzhizhaopin);//全部更新
        return R.ok();
    }
    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        jianzhizhaopinService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 提醒接口
     */
	@RequestMapping("/remind/{columnName}/{type}")
	public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request, 
						 @PathVariable("type") String type,@RequestParam Map<String, Object> map) {
		map.put("column", columnName);
		map.put("type", type);
		
		if(type.equals("2")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			Date remindStartDate = null;
			Date remindEndDate = null;
			if(map.get("remindstart")!=null) {
				Integer remindStart = Integer.parseInt(map.get("remindstart").toString());
				c.setTime(new Date()); 
				c.add(Calendar.DAY_OF_MONTH,remindStart);
				remindStartDate = c.getTime();
				map.put("remindstart", sdf.format(remindStartDate));
			}
			if(map.get("remindend")!=null) {
				Integer remindEnd = Integer.parseInt(map.get("remindend").toString());
				c.setTime(new Date());
				c.add(Calendar.DAY_OF_MONTH,remindEnd);
				remindEndDate = c.getTime();
				map.put("remindend", sdf.format(remindEndDate));
			}
		}
		
		Wrapper<JianzhizhaopinEntity> wrapper = new EntityWrapper<JianzhizhaopinEntity>();
		if(map.get("remindstart")!=null) {
			wrapper.ge(columnName, map.get("remindstart"));
		}
		if(map.get("remindend")!=null) {
			wrapper.le(columnName, map.get("remindend"));
		}

		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("shangjia")) {
			wrapper.eq("shangjiazhanghao", (String)request.getSession().getAttribute("username"));
		}

		int count = jianzhizhaopinService.selectCount(wrapper);
		return R.ok().put("count", count);
	}
	


}
