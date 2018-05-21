package com.framework.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.framework.constants.Constants;
import com.framework.entity.TStoryEntity;
import com.framework.model.StoreAddUpdateModel;
import com.framework.service.FileService;
import com.framework.service.TStoryService;
import com.framework.utils.DateUtil;
import com.framework.utils.PageUtils;
import com.framework.utils.R;
import com.framework.utils.ShiroUtils;
import com.framework.utils.StringUtil;


/**
 * 
 * 
 * @author R & D
 * @email 908350381@qq.com
 * @date 2018-05-14 11:40:45
 */
@Controller
@RequestMapping("/tstory")
public class TStoryController extends AbstractController{
	@Autowired
	private TStoryService tStoryService;
	
	@RequestMapping("/tstorylist")
	public String list(){
		return "tstory/tstory.html";
	}
	
	@RequestMapping("/tstory_add.html")
	public String add(){
		return "tstory/tstory_add.html";
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("tstory:list")
	public R list(Integer page, Integer limit){
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		
		//查询列表数据
		List<TStoryEntity> tStoryList = tStoryService.queryList(map);
		int total = tStoryService.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(tStoryList, total, limit, page);
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{id}")
	@RequiresPermissions("tstory:info")
	public R info(@PathVariable("id") Integer id){
		TStoryEntity tStory = tStoryService.queryObject(id);
		StoreAddUpdateModel model = new StoreAddUpdateModel();
		model.setContent(tStory.getContent());
		model.setIcon(tStory.getStoryIcon());
		model.setTitle(tStory.getStoryTitle());
		model.setId(id);
		return R.ok().put("tStory", model);
	}
	
	private File multipartToFile(MultipartFile multfile) throws IOException { 
	    CommonsMultipartFile cf = (CommonsMultipartFile)multfile;  
	    //这个myfile是MultipartFile的 
	    DiskFileItem fi = (DiskFileItem) cf.getFileItem(); 
	    File file = fi.getStoreLocation(); 
	    //手动创建临时文件 
	    if(file.length() < 100000){ 
	        File tmpFile = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") +  
	                file.getName()); 
	        multfile.transferTo(tmpFile); 
	        return tmpFile; 
	    } 
	    return file; 
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("tstory:save")
	public R save(@RequestParam("tStory")String tStory,@RequestParam("uFile")MultipartFile uploadFile) throws Exception{

		TStoryEntity story = new TStoryEntity();
		JSONObject viewModel = JSONObject.parseObject(tStory);
		int userid = ShiroUtils.getUserId().intValue();
		story.setCreateBy(userid);
		story.setCreateTime(DateUtil.getNowTimestamp());
		story.setUpdateBy(userid);
		story.setStoryTitle(viewModel.getString("title"));
		story.setUpdateTime(DateUtil.getNowTimestamp());
		story.setFlg(1);
		String htmlContent = StringUtil.formatHTML(viewModel.getString("title"), viewModel.getString("content"));
		story.setContent(htmlContent);
		//生成html
		FileService fs=new FileService();
		String uuid = UUID.randomUUID().toString();
		//生成html文件
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(Constants.FILE_HOST.DOCUMENT+uuid+".html"),"utf-8"),true);
			pw.println(htmlContent);
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//上传图片
		String logo = fs.upload(uploadFile, Constants.FILE_HOST.IMG, Constants.HOST.IMG);
		if(StringUtil.isNoneBlank(logo)){
			story.setStoryIcon(logo);
		}
		String contentUrl = Constants.HOST.DOCUMENT+uuid+".html";
		story.setDescUrl(contentUrl);
		tStoryService.save(story);
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("tstory:update")
	public R update(@RequestParam("tStory")String tStory,@RequestParam("uFile")MultipartFile uploadFile) throws Exception{
		TStoryEntity story = new TStoryEntity();
		JSONObject viewModel = JSONObject.parseObject(tStory);
		int userid = ShiroUtils.getUserId().intValue();
		story.setCreateBy(userid);
		story.setCreateTime(DateUtil.getNowTimestamp());
		story.setUpdateBy(userid);
		story.setStoryTitle(viewModel.getString("title"));
		story.setUpdateTime(DateUtil.getNowTimestamp());
		story.setFlg(1);
		String htmlContent = StringUtil.formatHTML(viewModel.getString("title"), viewModel.getString("content"));
		story.setContent(htmlContent);
		//生成html
		FileService fs=new FileService();
		String uuid = UUID.randomUUID().toString();
		//生成html文件
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(Constants.FILE_HOST.DOCUMENT+uuid+".html"),"utf-8"),true);
			pw.println(htmlContent);
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//上传图片
		String logo = fs.upload(uploadFile, Constants.FILE_HOST.IMG, Constants.HOST.IMG);
		if(StringUtil.isNoneBlank(logo)){
			story.setStoryIcon(logo);
		}
		String contentUrl = Constants.HOST.DOCUMENT+uuid+".html";
		story.setDescUrl(contentUrl);
		tStoryService.save(story);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("tstory:delete")
	public R delete(@RequestBody Integer[] ids){
		tStoryService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
