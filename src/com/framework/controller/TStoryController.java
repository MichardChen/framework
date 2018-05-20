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

import com.framework.constants.Constants;
import com.framework.entity.TStoryEntity;
import com.framework.model.StoreAddUpdateModel;
import com.framework.service.FileService;
import com.framework.service.TStoryService;
import com.framework.utils.DateUtil;
import com.framework.utils.ImageCompressZipUtil;
import com.framework.utils.ImageTools;
import com.framework.utils.PageUtils;
import com.framework.utils.R;
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
		model.setContent(tStory.getDescUrl());
		//model.setIcon(tStory.getStoryIcon());
		model.setTitle(tStory.getStoryTitle());
		model.setUrl(tStory.getDescUrl());
		return R.ok().put("tStory", model);
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("tstory:save")
	public R save(@RequestBody StoreAddUpdateModel tStory,@RequestParam("uFile")MultipartFile uFile){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		TStoryEntity story = new TStoryEntity();
		/*HttpSession session = request.getSession();
		int userid = (Integer)session.getAttribute("userId");*/
		story.setCreateBy((Integer)request.getAttribute("userId"));
		story.setCreateTime(DateUtil.getNowTimestamp());
		story.setUpdateBy((Integer)request.getAttribute("userId"));
		story.setUpdateTime(DateUtil.getNowTimestamp());
		File uploadFile = tStory.getIcon();
		story.setStoryIcon("");
		story.setStoryTitle(tStory.getTitle());
		story.setFlg(1);
		String htmlContent = StringUtil.formatHTML(tStory.getTitle(), tStory.getContent());
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
		String logo = "";
		//上传文件
		if(uploadFile != null){
			String fileName = uploadFile.getName();
			String[] names = fileName.split("\\.");
		    File t=new File(Constants.FILE_HOST.IMG+uuid+"."+names[1]);
		    logo = Constants.HOST.IMG+uuid+"."+names[1];
		    try{
		        t.createNewFile();
		    }catch(IOException e){
		        e.printStackTrace();
		    }
		    
		    fs.fileChannelCopy(uploadFile, t);
		    ImageCompressZipUtil.zipWidthHeightImageFile(uploadFile, t, ImageTools.getImgWidth(uploadFile), ImageTools.getImgHeight(uploadFile), 0.5f);
		    uploadFile.delete();
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
	public R update(@RequestBody TStoryEntity tStory){
		tStoryService.update(tStory);
		
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
