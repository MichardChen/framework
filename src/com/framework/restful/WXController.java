package com.framework.restful;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.lf5.LogLevelFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.framework.constants.Constants;
import com.framework.constants.Constants.CAR_TYPE;
import com.framework.dao.LocationCityDao;
import com.framework.dao.TBrandDao;
import com.framework.dao.TBrandSeriesDao;
import com.framework.dao.TCarImportDao;
import com.framework.dao.TCarLeaseDao;
import com.framework.dao.TCarSecondhandDao;
import com.framework.dao.TCodemstDao;
import com.framework.dto.ParamsDTO;
import com.framework.entity.LocationCityEntity;
import com.framework.entity.SysUserEntity;
import com.framework.entity.TBrandEntity;
import com.framework.entity.TBrandSeriesEntity;
import com.framework.entity.TCarImportEntity;
import com.framework.entity.TCarLeaseEntity;
import com.framework.entity.TCarSecondhandEntity;
import com.framework.entity.TCarouselEntity;
import com.framework.entity.TCartParam2Entity;
import com.framework.entity.TCartParamsEntity;
import com.framework.entity.TCodemstEntity;
import com.framework.entity.TFinanceCommitEntity;
import com.framework.entity.TFinanceEntity;
import com.framework.entity.TNewsEntity;
import com.framework.entity.TQuestionEntity;
import com.framework.entity.TSalecartEntity;
import com.framework.entity.TStoryEntity;
import com.framework.entity.TVertifyCodeEntity;
import com.framework.model.FinanceListModel;
import com.framework.model.StoryListModel;
import com.framework.pcmodel.PCBrandModel;
import com.framework.restmodel.BrandJsonModel;
import com.framework.restmodel.BrandModel;
import com.framework.restmodel.BrandSeriesMJsonModel;
import com.framework.restmodel.CarouselModel;
import com.framework.restmodel.ImportCarListModel;
import com.framework.restmodel.LeaseCarListModel;
import com.framework.restmodel.ListModelImportCar;
import com.framework.restmodel.ListModelLeaseCar;
import com.framework.restmodel.ListModelSecondhandCar;
import com.framework.restmodel.NewsListModel;
import com.framework.restmodel.SaleManageListModel;
import com.framework.restmodel.SecondHandCarListModel;
import com.framework.service.SysUserService;
import com.framework.service.TBrandSeriesService;
import com.framework.service.TBrandService;
import com.framework.service.TCarImportService;
import com.framework.service.TCarLeaseService;
import com.framework.service.TCarSecondhandService;
import com.framework.service.TCarouselService;
import com.framework.service.TCartParam2Service;
import com.framework.service.TCartParamsService;
import com.framework.service.TFinanceCommitService;
import com.framework.service.TFinanceService;
import com.framework.service.TNewsService;
import com.framework.service.TQuestionService;
import com.framework.service.TSalecartService;
import com.framework.service.TStoryService;
import com.framework.service.TVertifyCodeService;
import com.framework.utils.DateUtil;
import com.framework.utils.ReturnData;
import com.framework.utils.ShortMessageUtil;
import com.framework.utils.StringUtil;
import com.framework.utils.VertifyUtil;

@Controller
@RequestMapping("wxrest")
public class WXController extends RestfulController {

	@Autowired
	private TCarouselService carouselService;
	@Autowired
	private TBrandService brandService;
	@Autowired
	private TCarLeaseService leaseService;
	@Autowired
	private TBrandSeriesDao brandSeriesDao;
	@Autowired
	private TCarImportService importService;
	@Autowired
	private TCarSecondhandService secondService;
	@Autowired
	private LocationCityDao cityDao;
	@Autowired
	private TStoryService storyService;
	@Autowired
	private TNewsService newsService;
	@Autowired
	private TCodemstDao codeMstDao;
	@Autowired
	private TFinanceService financeService;
	@Autowired
	private TSalecartService saleCartService;
	@Autowired
	private TQuestionService questionService;
	@Autowired
	private TVertifyCodeService vertifyCodeService;
	@Autowired
	private TBrandSeriesService carSeriesService;
	@Autowired
	private TFinanceCommitService financeCommitService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private TCartParamsService paramsService;
	@Autowired
	private TCartParam2Service params2Service;
	@Autowired
	private TCarImportDao carImportDao;
	@Autowired
	private TBrandDao brandDao;
	@Autowired
	private TCarLeaseDao carLeaseDao;
	@Autowired
	private TCarSecondhandDao carSecondhandDao;

	@RequestMapping("/index")
	public void index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ParamsDTO dto = ParamsDTO.getInstance(request);
		ReturnData data = new ReturnData();
		// 查询轮播图
		List<TCarouselEntity> list = carouselService.queryListByTypeCd(Constants.CAROUSEL_TYPE.WX_INDEX);
		List<CarouselModel> models = new ArrayList<>();
		CarouselModel model = null;
		for (TCarouselEntity e : list) {
			model = new CarouselModel();
			model.setImgUrl(e.getImgUrl());
			model.setRealUrl(e.getRealUrl());
			models.add(model);
		}
		JSONObject json = new JSONObject();
		json.put("carousel", models);
		// 以租代购、二手车、平行进口车横幅
		TCarouselEntity entity = carouselService.queryByTypeCd(Constants.CAROUSEL_TYPE.CAR_LEASE_IMAGE);
		if (entity != null) {
			CarouselModel m1 = new CarouselModel();
			m1.setImgUrl(entity.getImgUrl());
			m1.setRealUrl(entity.getRealUrl());
			json.put("leaseCarImage", m1);
		} else {
			json.put("leaseCarImage", null);
		}
		TCarouselEntity entity1 = carouselService.queryByTypeCd(Constants.CAROUSEL_TYPE.CAR_IMPORT_IMAGE);
		if (entity1 != null) {
			CarouselModel m1 = new CarouselModel();
			m1.setImgUrl(entity1.getImgUrl());
			m1.setRealUrl(entity1.getRealUrl());
			json.put("importCarImage", m1);
		} else {
			json.put("importCarImage", null);
		}
		TCarouselEntity entity2 = carouselService.queryByTypeCd(Constants.CAROUSEL_TYPE.CAR_SECONDHAND_IMAGE);
		if (entity2 != null) {
			CarouselModel m1 = new CarouselModel();
			m1.setImgUrl(entity2.getImgUrl());
			m1.setRealUrl(entity2.getRealUrl());
			json.put("secondHandCarImage", m1);
		} else {
			json.put("secondHandCarImage", null);
		}
		// 车的图标
		Map<String, Object> map = new HashMap<>();
		map.put("offset", 0);
		map.put("limit", 9);
		List<TBrandEntity> brandsList = brandService.queryShowBrandList(1);
		List<BrandModel> brandModels = new ArrayList<>();
		BrandModel bm = null;
		for (TBrandEntity e : brandsList) {
			bm = new BrandModel();
			bm.setBrand(e.getBrand());
			bm.setId(e.getId());
			bm.setBrandIcon(e.getBrandIcon());
			brandModels.add(bm);
		}
		json.put("brandList", brandModels);
		// 以租代购
		Map<String, Object> carListMap = new HashMap<>();
		carListMap.put("offset", 0);
		carListMap.put("limit", 6);
		carListMap.put("flg", 1);

		List<TCarLeaseEntity> clList = leaseService.queryList(carListMap);
		List<TCarImportEntity> ciList = importService.queryList(carListMap);
		List<TCarSecondhandEntity> csList = secondService.queryList(carListMap);

		List<LeaseCarListModel> leaseList = new ArrayList<>();
		List<ImportCarListModel> importList = new ArrayList<>();
		List<SecondHandCarListModel> secondList = new ArrayList<>();

		LeaseCarListModel lcm = null;
		for (TCarLeaseEntity e : clList) {
			lcm = new LeaseCarListModel();
			
			lcm.setIcon(e.getIcon());
			lcm.setId(e.getId());
			
			if(e.getShowFlg()==1){
				lcm.setFirstPayment(StringUtil.formatCarPrice(e.getFirstPayment(),0));
				lcm.setMonthPayment(StringUtil.formatCarPrice(e.getMonthPayment(),1));
				lcm.setPeriod(StringUtil.toString(36));
			}
			if(e.getShowFlg()==2){
				lcm.setFirstPayment(StringUtil.formatCarPrice(e.getFirstPayment1(),0));
				lcm.setMonthPayment(StringUtil.formatCarPrice(e.getMonthPayment1(),1));
				lcm.setPeriod(StringUtil.toString(48));
			}
			if(e.getShowFlg()==3){
				lcm.setFirstPayment(StringUtil.formatCarPrice(e.getTfirstYearFirstPay(),0));
				lcm.setMonthPayment(StringUtil.formatCarPrice(e.getTfirstYearMonthPayment(),1));
				lcm.setPeriod(StringUtil.toString(e.getTperiods()));
			}
			//lcm.setFirstPayment(StringUtil.formatCarPrice(e.getFirstPayment(),0));
			//lcm.setMonthPayment(StringUtil.formatCarPrice(e.getMonthPayment(),1));
			TBrandSeriesEntity seriesEntity = brandSeriesDao.queryObject(e.getCarSeriesId());
			TBrandEntity brandEntity = brandService.queryObject(e.getBrand());
			lcm.setName(brandEntity.getBrand()+" "+seriesEntity.getCarSerial()+" "+e.getCarName());
			//lcm.setPeriod(StringUtil.toString(e.getPeriods()));
			lcm.setLabels(e.getLabels());
			leaseList.add(lcm);
		}

		ImportCarListModel icl = null;
		for (TCarImportEntity e : ciList) {
			icl = new ImportCarListModel();
			icl.setIcon(e.getIcon());
			icl.setId(e.getId());
			icl.setLabels(e.getLabels());
			TBrandSeriesEntity seriesEntity = brandSeriesDao.queryObject(e.getCarSeriesId());
			TBrandEntity brandEntity = brandService.queryObject(e.getBrand());
			icl.setName(brandEntity.getBrand()+" "+seriesEntity.getCarSerial()+" "+e.getCarName());
			icl.setNowPrice(StringUtil.formatCarPrice(e.getNowPrice(),0));
			icl.setPrimePrice(StringUtil.formatCarPrice(e.getMarketPrice(),0));
			icl.setLabels(e.getLabels());
			icl.setTitleLabel(e.getTitleLabel());
			importList.add(icl);
		}

		SecondHandCarListModel scl = null;
		for (TCarSecondhandEntity e : csList) {
			scl = new SecondHandCarListModel();
			scl.setIcon(e.getIcon());
			TBrandSeriesEntity seriesEntity = brandSeriesDao.queryObject(e.getCarSeriesId());
			TBrandEntity brandEntity = brandService.queryObject(e.getBrand());
			scl.setName(brandEntity.getBrand()+" "+seriesEntity.getCarSerial()+" "+e.getCarName());
			scl.setKilometers(StringUtil.toString(e.getKilomiters()));
			
			if(e.getShowFlg()==1){
				scl.setFirstPayment(StringUtil.formatCarPrice(e.getFirstPayment(), 0));
				scl.setMonthPayment(StringUtil.formatCarPrice(e.getMonthPayment(),1));
			}
			if(e.getShowFlg()==2){
				scl.setFirstPayment(StringUtil.formatCarPrice(e.getFirstPayment1(), 0));
				scl.setMonthPayment(StringUtil.formatCarPrice(e.getMonthPayment1(),1));
			}
			if(e.getShowFlg()==3){
				scl.setFirstPayment(StringUtil.formatCarPrice(e.getTfirstYearFirstPay(), 0));
				scl.setMonthPayment(StringUtil.formatCarPrice(e.getTfirstYearMonthPayment(),1));
			}
			
			//scl.setMonthPayment(StringUtil.formatCarPrice(e.getMonthPayment(),1));
			scl.setId(e.getId());
			scl.setDate(DateUtil.formatCNYM(e.getYear()));
			LocationCityEntity city = cityDao.queryObject(e.getCityId());
			scl.setCity(city == null ? "" : city.getName());
			secondList.add(scl);
		}

		json.put("leaseList", leaseList);
		json.put("importList", importList);
		json.put("secondList", secondList);

		// 车主故事
		List<TStoryEntity> stList = storyService.queryListData(carListMap);
		List<NewsListModel> storyList = new ArrayList<>();
		NewsListModel slm = null;
		for (TStoryEntity e : stList) {
			slm = new NewsListModel();
			slm.setIcon(e.getStoryIcon());
			slm.setId(e.getId());
			slm.setTitle(e.getStoryTitle());
			slm.setType("");
			slm.setUrl(e.getDescUrl());
			slm.setDate(DateUtil.formatCN(e.getCreateTime()));
			storyList.add(slm);
		}
		json.put("storyList", storyList);
		// 资讯
		List<TNewsEntity> snList = newsService.queryListData(carListMap);
		List<NewsListModel> newList = new ArrayList<>();
		NewsListModel nlm = null;
		for (TNewsEntity e : snList) {
			nlm = new NewsListModel();
			nlm.setIcon(e.getNewsLogo());
			nlm.setId(e.getId());
			nlm.setTitle(e.getNewsTitle());
			nlm.setUrl(e.getContentUrl());
			nlm.setDate(DateUtil.formatCN(e.getCreateTime()));
			TCodemstEntity mst = codeMstDao.queryByCode(e.getNewsTypeCd());
			if (mst != null) {
				nlm.setType(mst.getName());
			} else {
				nlm.setType("");
			}
			newList.add(nlm);
		}
		json.put("storyList", storyList);
		json.put("newList", newList);

		data.setData(json);
		data.setCode(Constants.STATUS_CODE.SUCCESS);
		data.setMessage("查询成功");
		renderJson(data, response);
	}

	// 查询所有品牌
	@RequestMapping("/queryAllBrand")
	public void queryAllBrand(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ParamsDTO dto = ParamsDTO.getInstance(request);
		ReturnData data = new ReturnData();
		JSONObject json = new JSONObject();
		List<TBrandEntity> list = brandService.queryAllList(1);
		Map<String, List<BrandModel>> map = new HashMap<>();
		for (TBrandEntity e : list) {
			String word = e.getWord();
			BrandModel bm = new BrandModel();
			bm.setBrand(e.getBrand());
			bm.setId(e.getId());
			bm.setBrandIcon(e.getBrandIcon());
			if (map.containsKey(word)) {
				// 包含
				List<BrandModel> value = map.get(word);
				value.add(bm);
				map.put(word, value);
			} else {
				// 不包含
				List<BrandModel> value = new ArrayList<>();
				value.add(bm);
				map.put(word, value);
			}
		}
		
		List<Map.Entry<String, List<BrandModel>>> infoIds =
			    new ArrayList<Map.Entry<String, List<BrandModel>>>(map.entrySet());
		
		Collections.sort(infoIds, new Comparator<Map.Entry<String, List<BrandModel>>>() {   
			@Override
			public int compare(Entry<String, List<BrandModel>> arg0, Entry<String, List<BrandModel>> arg1) {
				return (arg0.getKey()).toString().compareTo(arg1.getKey());
			}
		}); 
		Map<String, List<BrandModel>> map1 = new LinkedHashMap<>();
		for(Map.Entry<String, List<BrandModel>> e : infoIds) {
			System.out.println(e.getKey());
			map1.put(e.getKey(), map.get(e.getKey()));
		}
		
		json.put("brandList", map1);
		data.setData(json);
		data.setCode(Constants.STATUS_CODE.SUCCESS);
		data.setMessage("查询成功");
		renderJson(data, response);
	}

	// 资讯列表
	@RequestMapping("/queryNewsList")
	public void queryNewsList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ParamsDTO dto = ParamsDTO.getInstance(request);
		ReturnData data = new ReturnData();
		JSONObject json = new JSONObject();
		Map<String, Object> carListMap = new HashMap<>();
		carListMap.put("offset", dto.getPageSize() * (dto.getPageNum() - 1));
		carListMap.put("limit", dto.getPageSize());
		List<TNewsEntity> snList = newsService.queryListData(carListMap);
		List<NewsListModel> newList = new ArrayList<>();
		NewsListModel nlm = null;
		for (TNewsEntity e : snList) {
			nlm = new NewsListModel();
			nlm.setIcon(e.getNewsLogo());
			nlm.setId(e.getId());
			nlm.setTitle(e.getNewsTitle());
			nlm.setUrl(e.getContentUrl());
			nlm.setDate(DateUtil.formatCN(e.getCreateTime()));
			TCodemstEntity mst = codeMstDao.queryByCode(e.getNewsTypeCd());
			if (mst != null) {
				nlm.setType(mst.getName());
			} else {
				nlm.setType("");
			}
			newList.add(nlm);
		}
		json.put("newList", newList);

		data.setData(json);
		data.setCode(Constants.STATUS_CODE.SUCCESS);
		data.setMessage("查询成功");
		renderJson(data, response);
	}

	// 故事列表
	@RequestMapping("/queryStoryList")
	public void queryStoryList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ParamsDTO dto = ParamsDTO.getInstance(request);
		ReturnData data = new ReturnData();
		JSONObject json = new JSONObject();
		Map<String, Object> carListMap = new HashMap<>();
		carListMap.put("offset", dto.getPageSize() * (dto.getPageNum() - 1));
		carListMap.put("limit", dto.getPageSize());

		List<TStoryEntity> stList = storyService.queryListData(carListMap);
		List<NewsListModel> storyList = new ArrayList<>();
		NewsListModel slm = null;
		for (TStoryEntity e : stList) {
			slm = new NewsListModel();
			slm.setIcon(e.getStoryIcon());
			slm.setId(e.getId());
			slm.setTitle(e.getStoryTitle());
			slm.setType("");
			slm.setDate(DateUtil.formatCN(e.getCreateTime()));
			slm.setUrl(e.getDescUrl());
			storyList.add(slm);
		}
		json.put("storyList", storyList);
		data.setData(json);
		data.setCode(Constants.STATUS_CODE.SUCCESS);
		data.setMessage("查询成功");
		renderJson(data, response);
	}

	@RequestMapping("/queryFinanceList")
	public void queryFinanceList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ParamsDTO dto = ParamsDTO.getInstance(request);
		ReturnData data = new ReturnData();
		JSONObject json = new JSONObject();
		Map<String, Object> map = new HashMap<>();
		map.put("offset", dto.getPageSize() * (dto.getPageNum() - 1));
		map.put("limit", dto.getPageSize());
		map.put("status", 1);

		List<TFinanceEntity> stList = financeService.queryList(map);
		List<FinanceListModel> models = new ArrayList<>();
		FinanceListModel slm = null;
		for (TFinanceEntity e : stList) {
			slm = new FinanceListModel();
			slm.setId(e.getId());
			slm.setFinanceName(e.getName());
			slm.setMoneys(e.getLowRefund());
			slm.setPeriod(e.getTimeDistance());
			slm.setRate(e.getLowRate());
			slm.setIcon(e.getIcon());
			slm.setStandard(e.getStandard());
			models.add(slm);
		}
		json.put("financeList", models);
		data.setData(json);
		data.setCode(Constants.STATUS_CODE.SUCCESS);
		data.setMessage("查询成功");
		renderJson(data, response);
	}

	@RequestMapping("/saveSaleCart")
	public void saveSaleCart(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ParamsDTO dto = ParamsDTO.getInstance(request);
		ReturnData data = new ReturnData();
		JSONObject json = new JSONObject();
		TSalecartEntity entity = new TSalecartEntity();
		entity.setCreateBy(0);
		entity.setUpdateBy(0);
		entity.setCreateTime(DateUtil.getNowTimestamp());
		entity.setUpdateTime(DateUtil.getNowTimestamp());
		entity.setMark(dto.getMark());
		entity.setMobile(dto.getMobile());
		entity.setName(dto.getName());
		entity.setStatus(Constants.FEEDBACK_STATUS.STAY_HANDLE);
		saleCartService.save(entity);
		data.setData(json);
		data.setCode(Constants.STATUS_CODE.SUCCESS);
		data.setMessage("提交成功,我们会尽快联系您");
		renderJson(data, response);
	}

	@RequestMapping("/sendVertifyCode")
	public void sendVertifyCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ParamsDTO dto = ParamsDTO.getInstance(request);
		ReturnData data = new ReturnData();
		JSONObject json = new JSONObject();
		TVertifyCodeEntity vertifyCodeEntity = vertifyCodeService.queryObjectByMobile(dto.getMobile(),
				Constants.SHORT_MESSAGE_TYPE.SUMBIT_QUESTION);
		String code = VertifyUtil.getVertifyCode();
		if (vertifyCodeEntity != null) {

			vertifyCodeEntity.setCode(code);
			vertifyCodeEntity.setUpdateTime(DateUtil.getNowTimestamp());
			vertifyCodeEntity.setCodeTypeCd(Constants.SHORT_MESSAGE_TYPE.SUMBIT_QUESTION);
			vertifyCodeEntity.setExpireTime(DateUtil.getVertifyCodeExpireTime());
			int updateFlg = vertifyCodeService.update(vertifyCodeEntity);
			if (updateFlg != 0) {
				data.setCode(Constants.STATUS_CODE.SUCCESS);
				data.setMessage("发送成功");
				String shortMsg = "您的验证码是：" + code + "，10分钟内有效，请不要把验证码泄露给其他人。";
				ShortMessageUtil.sendsms(dto.getMobile(), shortMsg);
				renderJson(data, response);
				return;

			} else {
				data.setCode(Constants.STATUS_CODE.FAIL);
				data.setMessage("获取验证码失败，请重试");
				renderJson(data, response);
				return;
			}
		} else {
			TVertifyCodeEntity e = new TVertifyCodeEntity();
			e.setCode(code);
			e.setCodeTypeCd(Constants.SHORT_MESSAGE_TYPE.SUMBIT_QUESTION);
			e.setCreateTime(DateUtil.getNowTimestamp());
			e.setUpdateTime(DateUtil.getNowTimestamp());
			e.setExpireTime(DateUtil.getVertifyCodeExpireTime());
			e.setMobile(dto.getMobile());
			e.setUserTypeCd(Constants.USER_TYPE.USER_TYPE_CLIENT);
			int ret = vertifyCodeService.save(e);
			if (ret != 0) {
				data.setCode(Constants.STATUS_CODE.SUCCESS);
				data.setMessage("发送成功");
				String shortMsg = "您的验证码是：" + code + "，10分钟内有效，请不要把验证码泄露给其他人。";
				ShortMessageUtil.sendsms(dto.getMobile(), shortMsg);
				renderJson(data, response);
				return;
			} else {
				data.setCode(Constants.STATUS_CODE.FAIL);
				data.setMessage("获取验证码失败，请重试");
				renderJson(data, response);
				return;
			}
		}
	}

	@RequestMapping("/saveQuestion")
	public void saveQuestion(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ParamsDTO dto = ParamsDTO.getInstance(request);
		ReturnData data = new ReturnData();
		TQuestionEntity entity = new TQuestionEntity();
		entity.setCreateTime(DateUtil.getNowTimestamp());
		entity.setUpdateTime(DateUtil.getNowTimestamp());
		entity.setCreateBy(0);
		entity.setUpdateBy(0);
		entity.setCartId(dto.getCartId());
		entity.setCartflg(dto.getCartFlg());
		entity.setEmployeeId(dto.getEmployeeId());
		entity.setLinkMan(dto.getName());
		entity.setQuestion(dto.getQuestion());
		entity.setMobile(dto.getMobile());
		entity.setStatus(Constants.FEEDBACK_STATUS.STAY_HANDLE);
		// 判断验证码
		String code = dto.getCode();
		if (StringUtil.isBlank(code)) {
			data.setCode(Constants.STATUS_CODE.FAIL);
			data.setMessage("提交失败，验证码不能为空");
			renderJson(data, response);
			return;
		}
		TVertifyCodeEntity vertifyCodeEntity = vertifyCodeService.queryObjectByMobile(dto.getMobile(),
				Constants.SHORT_MESSAGE_TYPE.SUMBIT_QUESTION);
		if (vertifyCodeEntity != null) {
			Timestamp now = DateUtil.getNowTimestamp();
			Timestamp expireTime = vertifyCodeEntity.getExpireTime();
			if (expireTime == null) {
				data.setCode(Constants.STATUS_CODE.FAIL);
				data.setMessage("对不起，您还没有获取验证码");
				renderJson(data, response);
				return;
			}

			if ((expireTime != null) && (now.after(expireTime))) {
				// true，就是过期了
				data.setCode(Constants.STATUS_CODE.FAIL);
				data.setMessage("验证码过期了，请重新获取");
				renderJson(data, response);
				return;
			}

			if ((expireTime != null) && (expireTime.after(now))) {
				// 没有过期，获取数据库验证码
				String dcode = vertifyCodeEntity.getCode();
				if (!StringUtil.equals(code, dcode)) {
					// 验证码错误
					data.setCode(Constants.STATUS_CODE.FAIL);
					data.setMessage("请输入正确的验证码");
					renderJson(data, response);
					return;
				}
			}

			// 保存问题
			int ret = questionService.save(entity);
			if (ret != 0) {
				// 保存成功
				int updateRet = vertifyCodeService.updateExpireCode(vertifyCodeEntity.getId(),
						DateUtil.getNowTimestamp());
				if (updateRet != 0) {
					data.setCode(Constants.STATUS_CODE.SUCCESS);
					data.setMessage("提交成功,我们会尽快处理您的反馈");
					//
					SysUserEntity userEntity = sysUserService.queryObject(Long.valueOf(dto.getEmployeeId()));
					if(userEntity != null){
						String name = "您有客户向你发出了关于"+dto.getQuestion()+"的咨询，联系方式("+dto.getName()+"："+dto.getMobile()+")，请尽快联系客户。";
						ShortMessageUtil.sendsms(userEntity.getMobile(), name);
					}
					renderJson(data, response);
				} else {
					data.setCode(Constants.STATUS_CODE.FAIL);
					data.setMessage("提交失败,请重新提交");
					renderJson(data, response);
				}
			} else {
				data.setCode(Constants.STATUS_CODE.FAIL);
				data.setMessage("提交失败,请重新提交");
				renderJson(data, response);
			}
			return;
		} else {
			data.setCode(Constants.STATUS_CODE.FAIL);
			data.setMessage("对不起，您还没有获取验证码");
			renderJson(data, response);
			return;
		}
	}

	// 搜新车(以租代购列表)
	@RequestMapping("/queryLeaseCarList")
	public void queryLeaseCarList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ParamsDTO dto = ParamsDTO.getInstance(request);
		ReturnData data = new ReturnData();
		JSONObject json = new JSONObject();
		Map<String, Object> map = new HashMap<>();
		map.put("offset", dto.getPageSize() * (dto.getPageNum() - 1));
		map.put("limit", dto.getPageSize());
		map.put("brandId", dto.getBrandId());
		map.put("fromFirstPayment", dto.getFromFirstPayment());
		map.put("toFirstPayment", dto.getToFirstPayment());
		map.put("fromMonthPayment", dto.getFromMonthPayment());
		map.put("toMonthPayment", dto.getToMonthPayment());
		map.put("fromCarCost", dto.getFromCarCost());
		map.put("toCarCost", dto.getToCarCost());
		map.put("carLevelCd", dto.getCarLevelCd());
		map.put("flg", 1);

		List<TCarLeaseEntity> stList = leaseService.queryMobileTerminalList(map);
		List<ListModelLeaseCar> models = new ArrayList<>();
		ListModelLeaseCar model = null;
		for (TCarLeaseEntity e : stList) {
			model = new ListModelLeaseCar();
			model.setId(e.getId());
			TBrandSeriesEntity brand = carSeriesService.queryObject(e.getCarSeriesId());
			if (brand != null) {
				TBrandEntity brandEntity = brandService.queryObject(e.getBrand());
				if(brandEntity != null){
					model.setBrand(brandEntity.getBrand()+" "+brand.getCarSerial());
				}else{
					model.setBrand(brand.getCarSerial());
				}
			} else {
				model.setBrand("");
			}
			model.setCarName(e.getCarName());
			model.setLabels(e.getLabels());
			
			if(e.getShowFlg()==1){
				model.setFirstPayment(StringUtil.formatCarPrice(e.getFirstPayment(),0));
				model.setMonthPayment(StringUtil.formatCarPrice(e.getMonthPayment(),1));
			}
			if(e.getShowFlg()==2){
				model.setFirstPayment(StringUtil.formatCarPrice(e.getFirstPayment1(),0));
				model.setMonthPayment(StringUtil.formatCarPrice(e.getMonthPayment1(),1));
			}
			if(e.getShowFlg()==3){
				model.setFirstPayment(StringUtil.formatCarPrice(e.getTfirstYearFirstPay(),0));
				model.setMonthPayment(StringUtil.formatCarPrice(e.getTfirstYearMonthPayment(),1));
			}
			
			//model.setFirstPayment(StringUtil.formatCarPrice(e.getFirstPayment(), 0));
			//model.setMonthPayment(StringUtil.formatCarPrice(e.getMonthPayment(), 1));
			
			model.setGuidePrice(StringUtil.formatCarPrice(e.getFirmCost(), 0));
			
			model.setIcon(e.getIcon());
			model.setRealFirstPayment(StringUtil.formatCarPrice(e.getRealFirstPayment(), 0));
			models.add(model);
		}
		json.put("leaseCarList", models);
		data.setData(json);
		data.setCode(Constants.STATUS_CODE.SUCCESS);
		data.setMessage("查询成功");
		renderJson(data, response);
	}

	// 会淘车
	@RequestMapping("/querySecondhondCarList")
	public void querySecondhondCarList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ParamsDTO dto = ParamsDTO.getInstance(request);
		ReturnData data = new ReturnData();
		JSONObject json = new JSONObject();
		Map<String, Object> map = new HashMap<>();
		map.put("offset", dto.getPageSize() * (dto.getPageNum() - 1));
		map.put("limit", dto.getPageSize());
		map.put("brandId", dto.getBrandId());
		map.put("fromFirstPayment", dto.getFromFirstPayment());
		map.put("toFirstPayment", dto.getToFirstPayment());
		map.put("fromMonthPayment", dto.getFromMonthPayment());
		map.put("toMonthPayment", dto.getToMonthPayment());
		map.put("fromCarCost", dto.getFromCarCost());
		map.put("toCarCost", dto.getToCarCost());
		map.put("carLevelCd", dto.getCarLevelCd());
		map.put("flg", 1);

		List<TCarSecondhandEntity> stList = secondService.queryMobileTerminalList(map);
		List<ListModelSecondhandCar> models = new ArrayList<>();
		ListModelSecondhandCar model = null;
		for (TCarSecondhandEntity e : stList) {
			model = new ListModelSecondhandCar();
			model.setId(e.getId());
			TBrandSeriesEntity brand = carSeriesService.queryObject(e.getCarSeriesId());
			if (brand != null) {
				TBrandEntity brandEntity = brandService.queryObject(e.getBrand());
				if(brandEntity != null){
					model.setBrand(brandEntity.getBrand()+" "+brand.getCarSerial());
				}else{
					model.setBrand(brand.getCarSerial());
				}
			} else {
				model.setBrand("");
			}
			LocationCityEntity cityEntity = cityDao.queryObject(e.getCityId());
			if(cityEntity != null) {
				model.setCity(cityEntity.getName());
			}else {
				model.setCity("");
			}
			
			model.setCarName(e.getCarName());
			model.setLabels(e.getTitleLabel());
			
			if(e.getShowFlg()==1){
				model.setFirstPayment(StringUtil.formatCarPrice(e.getFirstPayment(), 0));
				model.setMonthPayment(StringUtil.formatCarPrice(e.getMonthPayment(),1));
			}
			if(e.getShowFlg()==2){
				model.setFirstPayment(StringUtil.formatCarPrice(e.getFirstPayment1(), 0));
				model.setMonthPayment(StringUtil.formatCarPrice(e.getMonthPayment1(),1));
			}
			if(e.getShowFlg()==3){
				model.setFirstPayment(StringUtil.formatCarPrice(e.getTfirstYearFirstPay(), 0));
				model.setMonthPayment(StringUtil.formatCarPrice(e.getTfirstYearMonthPayment(),1));
			}
			
			model.setIcon(e.getIcon());
			model.setDate(DateUtil.formatCNYM(e.getYear()));
			model.setKilomiters(StringUtil.toMoneyString(e.getKilomiters()) + "万公里");
			models.add(model);
		}
		json.put("secondhandCarList", models);
		data.setData(json);
		data.setCode(Constants.STATUS_CODE.SUCCESS);
		data.setMessage("查询成功");
		renderJson(data, response);
	}

	// 平行进口列表
	@RequestMapping("/queryImportCarList")
	public void queryImportCarList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ParamsDTO dto = ParamsDTO.getInstance(request);
		ReturnData data = new ReturnData();
		JSONObject json = new JSONObject();
		Map<String, Object> map = new HashMap<>();
		map.put("offset", dto.getPageSize() * (dto.getPageNum() - 1));
		map.put("limit", dto.getPageSize());
		map.put("brandId", dto.getBrandId());
		map.put("flg", 1);

		List<TCarImportEntity> stList = importService.queryImportCartList(map);
		List<ListModelImportCar> models = new ArrayList<>();
		ListModelImportCar model = null;
		for (TCarImportEntity e : stList) {
			model = new ListModelImportCar();
			model.setId(e.getId());
			TBrandSeriesEntity brand = carSeriesService.queryObject(e.getCarSeriesId());
			TBrandEntity brandEntity = brandService.queryObject(e.getBrand());
			if (brand != null) {
				if(brandEntity != null){
					model.setBrand(brandEntity.getBrand()+" "+brand.getCarSerial());
					model.setCarName(brandEntity.getBrand()+brand.getCarSerial()+" "+e.getCarName());
				}else{
					model.setBrand(brand.getCarSerial());
					model.setCarName(brand.getCarSerial()+" "+e.getCarName());
				}
			} else {
				model.setBrand("");
				model.setCarName(e.getCarName());
			}
			
			model.setNowPrice(StringUtil.formatCarPrice(e.getNowPrice(), 0));
			model.setMarkPrice(StringUtil.formatCarPrice(e.getMarketPrice(), 0));
			model.setSaveMoneys(StringUtil.formatCarPrice(e.getMaxSave(), 0));
			model.setIcon(e.getIcon());
			models.add(model);
		}
		
		List<TBrandEntity> list = brandService.queryImportBrandList(1);
		List<BrandModel> importBrands = new ArrayList<>();
		BrandModel importBrand = null;
		for(TBrandEntity entity : list){
			importBrand = new BrandModel();
			importBrand.setBrand(entity.getBrand());
			importBrand.setId(entity.getId());
			importBrand.setBrandIcon(entity.getBrandIcon());
			importBrands.add(importBrand);
		}
		
		json.put("brandList", importBrands);
		
		json.put("importCarList", models);
		data.setData(json);
		data.setCode(Constants.STATUS_CODE.SUCCESS);
		data.setMessage("查询成功");
		renderJson(data, response);
	}
	
	@RequestMapping("/queryAllBrandData")
	public void queryAllBrandData(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ParamsDTO dto = ParamsDTO.getInstance(request);
		ReturnData data = new ReturnData();
		JSONObject json = new JSONObject();
		List<TBrandEntity> list = brandService.queryAllList(1);
		List<BrandJsonModel> brandList = new ArrayList<>();
		BrandJsonModel brandModel = null;
		for(TBrandEntity entity : list){
			brandModel = new BrandJsonModel();
			brandModel.setId(entity.getId());
			brandModel.setName(entity.getBrand());
			brandModel.setPid(0);
			
			
			BrandSeriesMJsonModel seriesModel = null;
			List<BrandSeriesMJsonModel> models = new ArrayList<>();
			List<TBrandSeriesEntity> sList = brandSeriesDao.queryCarSeriesList(entity.getId());
			for(TBrandSeriesEntity b : sList) {
				seriesModel = new BrandSeriesMJsonModel();
				seriesModel.setId(b.getId());
				seriesModel.setName(b.getCarSerial());
				models.add(seriesModel);
			}
			brandModel.setModels(models);
			brandList.add(brandModel);
		}
		json.put("brandList", brandList);
		data.setData(json);
		data.setCode(Constants.STATUS_CODE.SUCCESS);
		data.setMessage("查询成功");
		renderJson(data, response);
	}
	
	@RequestMapping("/submitConsult")
	public void submitConsult(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ParamsDTO dto = ParamsDTO.getInstance(request);
		ReturnData data = new ReturnData();
		String code = dto.getCode();
		String mobile = dto.getMobile();
		TVertifyCodeEntity vCode = vertifyCodeService.queryObjectByMobile(mobile,Constants.SHORT_MESSAGE_TYPE.SUMBIT_QUESTION);
		Timestamp expireTime = vCode == null ? null : (Timestamp)vCode.getExpireTime();
		Timestamp now = DateUtil.getNowTimestamp();
		if(expireTime == null){
			data.setCode(Constants.STATUS_CODE.FAIL);
			data.setMessage("对不起，您还没有获取验证码");
			renderJson(data, response);
			return;
		}
		
		if((expireTime != null)&&(now.after(expireTime))){
			//true，就是过期了
			data.setCode(Constants.STATUS_CODE.FAIL);
			data.setMessage("验证码过期了，请重新获取");
			renderJson(data, response);
			return;
		}
		
		if((expireTime != null) && (expireTime.after(now))){
			//没有过期，获取数据库验证码
			String dcode = vCode.getCode();
			if(!StringUtil.equals(code, dcode)){
				//验证码错误
				data.setCode(Constants.STATUS_CODE.FAIL);
				data.setMessage("请输入正确的验证码");
				renderJson(data, response);
				return;
			}
		}
		
		TQuestionEntity entity = new TQuestionEntity();
		
		entity.setQuestion("从微信小程序提交的汽车立即咨询");
		entity.setCartId(dto.getCartId());
		entity.setLinkMan(dto.getName());
		entity.setMobile(dto.getMobile());
		entity.setCreateBy(0);
		entity.setCreateTime(DateUtil.getNowTimestamp());
		entity.setUpdateTime(DateUtil.getNowTimestamp());
		entity.setUpdateBy(0);
		entity.setStatus(Constants.FEEDBACK_STATUS.STAY_HANDLE);
		entity.setTypeCd(Constants.QUESTION_TYPE.XCX);
		entity.setCartflg(dto.getCartFlg());
		
		int ret = questionService.save(entity);
		if(ret != 0){
			//更新验证码过期
			vertifyCodeService.updateExpireCode(vCode.getId(), DateUtil.getNowTimestamp());
			data.setCode(Constants.STATUS_CODE.SUCCESS);
			data.setMessage("提交成功,我们会尽快联系您");
			
			//给工作人员发短信
			int cartFlg = dto.getCartFlg();
			//SysUserEntity userEntity = sysUserService.queryObject(Long.valueOf(dto.getEmployeeId()));
			String name = "";
			if(cartFlg == 2){
				//以租代购
				TCarLeaseEntity e = carLeaseDao.queryObject(dto.getCartId());
				if(entity != null){
					TBrandEntity brandEntity = brandDao.queryObject(e.getBrand());
					if(brandEntity != null){
						name = brandEntity.getBrand();
					}
					TBrandSeriesEntity brandSeriesEntity = brandSeriesDao.queryObject(e.getCarSeriesId());
					if(brandSeriesEntity != null){
						name = name + brandSeriesEntity.getCarSerial();
					}
				}
			}else if(cartFlg == 3){
				//会淘车
				TCarSecondhandEntity e = carSecondhandDao.queryObject(dto.getCartId());
				if(entity != null){
					TBrandEntity brandEntity = brandDao.queryObject(e.getBrand());
					if(brandEntity != null){
						name = brandEntity.getBrand();
					}
					TBrandSeriesEntity brandSeriesEntity = brandSeriesDao.queryObject(e.getCarSeriesId());
					if(brandSeriesEntity != null){
						name = name + brandSeriesEntity.getCarSerial();
					}
				}
			}else if(cartFlg == 4){
				//平行进口车
				TCarImportEntity e = carImportDao.queryObject(dto.getCartId());
				if(entity != null){
					TBrandEntity brandEntity = brandDao.queryObject(e.getBrand());
					if(brandEntity != null){
						name = brandEntity.getBrand();
					}
					TBrandSeriesEntity brandSeriesEntity = brandSeriesDao.queryObject(e.getCarSeriesId());
					if(brandSeriesEntity != null){
						name = name + brandSeriesEntity.getCarSerial();
					}
				}
			}
				TCodemstEntity phone = codeMstDao.queryByCode("130001");
				if(phone != null){
					String[] phones = phone.getData2().split(",");
					if(phones.length > 0){
						Random random = new Random();
						if(phones.length == 1){
							name = "您有客户向你发出了关于"+name+"的咨询，联系方式("+dto.getName()+"："+dto.getMobile()+")，请尽快联系客户。";
							ShortMessageUtil.sendsms(phones[0], name);
						}else{
							name = "您有客户向你发出了关于"+name+"的咨询，联系方式("+dto.getName()+"："+dto.getMobile()+")，请尽快联系客户。";
							ShortMessageUtil.sendsms(phones[random.nextInt(phones.length)], name);
						}
						
					}
				}
			renderJson(data, response);
			return;
		}else{
			data.setCode(Constants.STATUS_CODE.FAIL);
			data.setMessage("提交失败,请重新提交");
			renderJson(data, response);
			return;
		}
	}
	
	//提交金融申请
	@RequestMapping("/submitFinanceApply")
	public void submitFinanceApply(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ParamsDTO dto = ParamsDTO.getInstance(request);
		TFinanceCommitEntity entity = new TFinanceCommitEntity();
		entity.setFinanceId(dto.getFinanceId());
		entity.setAge(dto.getAge());
		entity.setBrandId(dto.getBrandId());
		entity.setBrandSeriesId(dto.getBrandSeriesId());
		entity.setProvinceId(dto.getProvinceId());
		entity.setCityId(dto.getCityId());
		entity.setName(dto.getName());
		entity.setIdcardNo(dto.getCardNo());
		entity.setSex(dto.getSex());
		entity.setMobile(dto.getMobile());
		entity.setMark(dto.getMark());
		entity.setCreateTime(DateUtil.getNowTimestamp());
		entity.setUpdateTime(DateUtil.getNowTimestamp());
		entity.setUpdateBy(0);
		entity.setStatus(Constants.FEEDBACK_STATUS.STAY_HANDLE);
		
		int ret = financeCommitService.save(entity);
		ReturnData data = new ReturnData();
		if(ret != 0){
			data.setCode(Constants.STATUS_CODE.SUCCESS);
			data.setMessage("提交成功,我们会尽快处理您的申请");
			renderJson(data, response);
			return;
		}else{
			data.setCode(Constants.STATUS_CODE.FAIL);
			data.setMessage("提交失败,请重新提交");
			renderJson(data, response);
			return;
		}
	}
	
	//销售经理列表
	@RequestMapping("/querySaleManageList")
	public void querySaleManageList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ParamsDTO dto = ParamsDTO.getInstance(request);
		ReturnData data = new ReturnData();
		JSONObject json = new JSONObject();
		List<SysUserEntity> list = sysUserService.querySaleManager(dto.getPageSize()*(dto.getPageNum()-1)
																  ,dto.getPageSize()
																  ,2);
		
		List<SaleManageListModel> saleManageList = new ArrayList<>();
		SaleManageListModel model = null;
		for(SysUserEntity entity : list){
			model = new SaleManageListModel();
			model.setId(entity.getUserId());
			model.setExpertFlg(entity.getExpertFlg());
			model.setIcon(entity.getIcon());
			model.setIntroduce(entity.getIntroduce());
			model.setSkill(entity.getSkill());
			model.setName(entity.getRealName());
			model.setMobile(entity.getMobile());
			saleManageList.add(model);
		}
		json.put("saleManageList", saleManageList);
		data.setData(json);
		data.setCode(Constants.STATUS_CODE.SUCCESS);
		data.setMessage("查询成功");
		renderJson(data, response);
	}
	
	//平行进口车品牌查询
	@RequestMapping("/queryImportCarBrandList")
	public void queryImportCarBrandList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ParamsDTO dto = ParamsDTO.getInstance(request);
		ReturnData data = new ReturnData();
		JSONObject json = new JSONObject();
		List<TBrandEntity> list = brandService.queryImportBrandList(1);
		/*List<BrandModel> models = new ArrayList<>();
		BrandModel model = null;
		for(TBrandEntity entity : list){
			model = new BrandModel();
			model.setBrand(entity.getBrand());
			model.setId(entity.getId());
			model.setBrandIcon(entity.getBrandIcon());
			
			models.add(model);
		}
		*/
		Map<String, List<BrandModel>> map = new HashMap<>();
		for(TBrandEntity e : list){
			String word = e.getWord();
			BrandModel bm = new BrandModel();
			bm.setBrand(e.getBrand());
			bm.setId(e.getId());
			bm.setBrandIcon(e.getBrandIcon());
			if(map.containsKey(word)){
				//包含
				List<BrandModel> value = map.get(word);
				value.add(bm);
				map.put(word, value);
			}else{
				//不包含
				List<BrandModel> value = new ArrayList<>();
				value.add(bm);
				map.put(word, value);
			}
		}
		
		List<Map.Entry<String, List<BrandModel>>> infoIds =
			    new ArrayList<Map.Entry<String, List<BrandModel>>>(map.entrySet());
		
		Collections.sort(infoIds, new Comparator<Map.Entry<String, List<BrandModel>>>() {   
			@Override
			public int compare(Entry<String, List<BrandModel>> arg0, Entry<String, List<BrandModel>> arg1) {
				return (arg0.getKey()).toString().compareTo(arg1.getKey());
			}
		}); 
		Map<String, List<BrandModel>> map1 = new LinkedHashMap<>();
		for(Map.Entry<String, List<BrandModel>> e : infoIds) {
			System.out.println(e.getKey());
			map1.put(e.getKey(), map.get(e.getKey()));
		}
		
		json.put("brandList", map1);
		data.setData(json);
		data.setCode(Constants.STATUS_CODE.SUCCESS);
		data.setMessage("查询成功");
		renderJson(data, response);
	}
	
	//平行进口车详情接口
	/**
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/queryImportCarDetail")
	public void queryImportCarDetail(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ParamsDTO dto = ParamsDTO.getInstance(request);
		ReturnData data = new ReturnData();
		JSONObject json = new JSONObject();
		TCarImportEntity car = importService.queryObject(dto.getCartId());
		if(car == null){
			data.setCode(Constants.STATUS_CODE.FAIL);
			data.setMessage("您来晚啦，此车已下架");
			renderJson(data, response);
			return;
		}
		ImportCartDetailModel model = new ImportCartDetailModel();
		model.setFavourMoney(StringUtil.formatCarPrice(car.getFavourMoney(), 0));
		model.setCartId(car.getId());
		TBrandEntity brand = brandService.queryObject(car.getBrand());
		if(brand != null){
			TBrandSeriesEntity brandSeriesEntity = carSeriesService.queryObject(car.getCarSeriesId());
			if(brandSeriesEntity != null){
				model.setCarName(brand.getBrand()+brandSeriesEntity.getCarSerial()+" "+car.getCarName());
			}else{
				model.setCarName(car.getCarName());
			}
		}else{
			model.setCarName(car.getCarName());
		}
		TBrandSeriesEntity seriesEntity = brandSeriesDao.queryObject(car.getCarSeriesId());
		if(seriesEntity != null){
			model.setCarSeriesName(seriesEntity.getCarSerial());
		}else{
			model.setCarSeriesName("");
		}
		model.setIcons(StringUtil.splitImg(car.getPcIcon(), ","));
		model.setColors(car.getCarColor());
		model.setReferenPrice(StringUtil.formatCarPrice(car.getNowPrice(),0));
		model.setMarketPrice(StringUtil.formatCarPrice(car.getMarketPrice(), 0));
		model.setMaxSavePrice(StringUtil.formatCarPrice(car.getMaxSave(), 0));
		TCodemstEntity mst = codeMstDao.queryByCode(car.getCarClassCd());
		if(mst != null){
			model.setClassType(mst.getName());
		}
		TCodemstEntity level = codeMstDao.queryByCode(car.getCarLevelCd());
		if(level != null){
			model.setCartType(level.getName());
		}
		model.setDescUrl(StringUtil.cutBodyHeader(car.getContent()));
		TCartParamsEntity params = paramsService.queryObjectByCartId(car.getId(),Constants.CAR_SALE_TYPE.IMPORT);
		
		if(params != null){
			model.setCheshenjiegou(params.getCheshenjiegou());
			model.setFadongjixinghao(params.getFadongjixinghao());
			model.setQudongtype(params.getQudongtype());
			model.setHeight(params.getHeight());
			model.setWidth(params.getWidth());
			model.setLength(params.getLength());
			model.setBiansuxiangtype(params.getBiansuxiangtype());
			model.setRanliaotype(params.getRanliaotype());
			model.setZhuchezhidongtype(params.getZhuchezhidongtype());
			model.setPailiang(params.getPailiang()+"mL");
		}
		//获取常见问题图片
		TCarouselEntity oftenQuestionUrl = carouselService.queryByTypeCd(Constants.CAROUSEL_TYPE.IMPORT_CAR_OFTENQUESTION);
		if(oftenQuestionUrl != null){
			model.setOftenQuestionUrl(oftenQuestionUrl.getImgUrl());
		}
		//客服电话
		TCodemstEntity kefuTel = codeMstDao.queryByCode(Constants.TEL_TYPE.KEFU);
		if(kefuTel != null){
			model.setCompanyMobile(kefuTel.getData2());
		}
		
		json.put("carDetail", model);
		
		/*TCartParamsEntity params1 = paramsService.queryObjectByCartId(dto.getCartId(),Constants.CAR_SALE_TYPE.IMPORT);
		TCartParam2Entity params2 = params2Service.queryObjectByCartIdType(dto.getCartId(),Constants.CAR_SALE_TYPE.IMPORT);
		
		json.put("params1", params1);
		json.put("params2", params2);*/
		
		
		data.setData(json);
		data.setCode(Constants.STATUS_CODE.SUCCESS);
		data.setMessage("查询成功");
		renderJson(data, response);
	}
	
	//会淘车详情
	@RequestMapping("/querySecondhandCarDetail")
	public void querySecondhandCarDetail(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ParamsDTO dto = ParamsDTO.getInstance(request);
		ReturnData data = new ReturnData();
		JSONObject json = new JSONObject();
		TCarSecondhandEntity car = secondService.queryObject(dto.getCartId());
		if(car == null){
			data.setCode(Constants.STATUS_CODE.FAIL);
			data.setMessage("您来晚啦，此车已下架");
			renderJson(data, response);
			return;
		}
		SecondhandCartDetailModel model = new SecondhandCartDetailModel();
		model.setCartId(car.getId());
		TBrandEntity brand = brandService.queryObject(car.getBrand());
		if(brand != null){
			TBrandSeriesEntity brandSeriesEntity = carSeriesService.queryObject(car.getCarSeriesId());
			if(brandSeriesEntity != null){
				model.setCarName(brand.getBrand()+brandSeriesEntity.getCarSerial()+" "+car.getCarName());
			}else{
				model.setCarName(car.getCarName());
			}
		}else{
			model.setCarName(car.getCarName());
		}
		model.setPeriods(StringUtil.toString(car.getPeriods()));
		TBrandSeriesEntity seriesEntity = brandSeriesDao.queryObject(car.getCarSeriesId());
		if(seriesEntity != null){
			model.setCarSeriesName(seriesEntity.getCarSerial());
		}else{
			model.setCarSeriesName("");
		}
		
		if(car.getFirstPayment() != null){
			model.setFlg1(1);
		}else{
			model.setFlg1(0);
		}
		if(car.getFirstPayment1() != null){
			model.setFlg2(1);
		}else{
			model.setFlg2(0);
		}
		if(car.getTfirstYearFirstPay() != null){
			model.setFlg3(1);
		}else{
			model.setFlg3(0);
		}
		
		model.setIcons(StringUtil.splitImg(car.getPcIcon(), ","));
		model.setLabels(car.getLabels());
		model.setFirstPayment(StringUtil.formatCarPrice(car.getFirstPayment(),0));
		model.setMonthPayment(StringUtil.formatCarPrice(car.getMonthPayment(), 1));
		model.setContainTaxPrice(StringUtil.formatCarPrice(car.getCarTaxCost(), 0));
		model.setDescUrl(StringUtil.cutBodyHeader(car.getContent()));
		model.setYear(DateUtil.formatCNYM(car.getYear()));
		model.setKilomiter(StringUtil.formatCarPrice(car.getKilomiters(), 0)+"公里");
		LocationCityEntity city = cityDao.queryObject(car.getCityId());
		if(city != null){
			model.setCity(city.getName());
		}
		
		
		/////0724修改
		//48期首付和月供、备注、分期数
		model.setFirstPayment1(StringUtil.formatCarPrice(car.getFirstPayment1(),0));
		model.setMonthPayment1(StringUtil.formatCarPrice(car.getMonthPayment1(), 1));
		
		//1+3首年首付、首年月供、一年后分期数、一年后分期月供
		model.setTfirstYearFirstPay(StringUtil.formatCarPrice(car.getTfirstYearFirstPay(),0));
		model.setTfirstYearMonthPayment(StringUtil.formatCarPrice(car.getTfirstYearMonthPayment(),1));
		model.setTperiods(StringUtil.toString(car.getTperiods()));
		model.setTmonthPayment(StringUtil.formatCarPrice(car.getTmonthPayment(),1));
		model.setFinalPayment(StringUtil.formatCarPrice(car.getFinalPayment(), 0));
		
		model.setBuyPay(StringUtil.formatCarPrice(car.getRealFirstPayment(),0));
		model.setServiceFee(StringUtil.formatCarPrice(car.getServiceFee(),1));
		/////////////////
			
		TCartParamsEntity params = paramsService.queryObjectByCartId(car.getId(),Constants.CAR_SALE_TYPE.SECONDHAND);
		
		if(params != null){
			model.setCheshenjiegou(params.getCheshenjiegou());
			model.setFadongjixinghao(params.getFadongjixinghao());
			model.setQudongtype(params.getQudongtype());
			model.setHeight(params.getHeight());
			model.setWidth(params.getWidth());
			model.setLength(params.getLength());
			model.setBiansuxiangtype(params.getBiansuxiangtype());
			model.setRanliaotype(params.getRanliaotype());
			model.setZhuchezhidongtype(params.getZhuchezhidongtype());
			model.setPailiang(params+"mL");
		}
		//获取购买说明
		TCarouselEntity buyMark = carouselService.queryByTypeCd(Constants.CAROUSEL_TYPE.SECONDHAND_CAR_BUYMARK);
		if(buyMark != null){
			model.setBuyMarkUrl(buyMark.getImgUrl());
		}
		//购买须知
		TCarouselEntity buyKnow = carouselService.queryByTypeCd(Constants.CAROUSEL_TYPE.SECONDHAND_CAR_BUYKONW);
		if(buyKnow != null){
			model.setBuyKnowUrl(buyKnow.getImgUrl());
		}
		//客服电话
		TCodemstEntity kefuTel = codeMstDao.queryByCode(Constants.TEL_TYPE.KEFU);
		if(kefuTel != null){
			model.setCompanyMobile(kefuTel.getData2());
		}
		
		json.put("carDetail", model);
		
		/*TCartParamsEntity params1 = paramsService.queryObjectByCartId(dto.getCartId(),Constants.CAR_SALE_TYPE.SECONDHAND);
		TCartParam2Entity params2 = params2Service.queryObjectByCartIdType(dto.getCartId(),Constants.CAR_SALE_TYPE.SECONDHAND);
		
		json.put("params1", params1);
		json.put("params2", params2);*/
		
		
		data.setData(json);
		data.setCode(Constants.STATUS_CODE.SUCCESS);
		data.setMessage("查询成功");
		renderJson(data, response);
	}
	//惠搜车详情
	@RequestMapping("/queryLeaseCarDetail")
	public void queryLeaseCarDetail(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ParamsDTO dto = ParamsDTO.getInstance(request);
		ReturnData data = new ReturnData();
		JSONObject json = new JSONObject();
		TCarLeaseEntity car = leaseService.queryObject(dto.getCartId());
		if(car == null){
			data.setCode(Constants.STATUS_CODE.FAIL);
			data.setMessage("您来晚啦，此车已下架");
			renderJson(data, response);
			return;
		}
		LeaseCarDetailModel model = new LeaseCarDetailModel();
		model.setCartId(car.getId());
		TBrandEntity brand = brandService.queryObject(car.getBrand());
		if(brand != null){
			TBrandSeriesEntity brandSeriesEntity = carSeriesService.queryObject(car.getCarSeriesId());
			if(brandSeriesEntity != null){
				model.setCarName(brand.getBrand()+brandSeriesEntity.getCarSerial()+" "+car.getCarName());
			}else{
				model.setCarName(car.getCarName());
			}
		}else{
			model.setCarName(car.getCarName());
		}
		TBrandSeriesEntity seriesEntity = brandSeriesDao.queryObject(car.getCarSeriesId());
		if(seriesEntity != null){
			model.setCarSeriesName(seriesEntity.getCarSerial());
		}else{
			model.setCarSeriesName("");
		}
		
		if(car.getFirstPayment() != null){
			model.setFlg1(1);
		}else{
			model.setFlg1(0);
		}
		if(car.getFirstPayment1() != null){
			model.setFlg2(1);
		}else{
			model.setFlg2(0);
		}
		if(car.getTfirstYearFirstPay() != null){
			model.setFlg3(1);
		}else{
			model.setFlg3(0);
		}
		
		model.setIcons(StringUtil.splitImg(car.getPcIcon(), ","));
		model.setFirmPrice(StringUtil.formatCarPrice(car.getFirmCost(),0));
		model.setCarInfo(car.getCarTypeInfo());
		//48期首付和月供、备注、分期数
		model.setFirstPayment(StringUtil.formatCarPrice(car.getFirstPayment(),0));
		model.setMonthPayment(StringUtil.formatCarPrice(car.getMonthPayment(), 1));
		model.setMark(car.getMark());
		model.setPeriods(car.getPeriods());
		
			
		//36期首付、月供、备注、分期数
		model.setFirstPayment1(StringUtil.formatCarPrice(car.getFirstPayment1(),0));
		model.setMonthPayment1(StringUtil.formatCarPrice(car.getMonthPayment1(), 1));
		model.setMark1(car.getMark());
		model.setPeriods1(car.getPeriods());
		
		//1+3首年首付、首年月供、一年后分期数、一年后分期月供
		model.setTfirstYearFirstPay(StringUtil.formatCarPrice(car.getTfirstYearFirstPay(),0));
		model.setTfirstYearMonthPayment(StringUtil.formatCarPrice(car.getTfirstYearMonthPayment(),1));
		model.setTperiods(car.getTperiods());
		model.setTmonthPayment(StringUtil.formatCarPrice(car.getTmonthPayment(),1));
		model.setFinalPayment(StringUtil.formatCarPrice(car.getFinalPayment(), 0));
		
		model.setBuyPay(StringUtil.formatCarPrice(car.getRealFirstPayment(),0));
		model.setServiceFee(StringUtil.formatCarPrice(car.getServiceFee(),1));
		//客服电话
		TCodemstEntity kefuTel = codeMstDao.queryByCode(Constants.TEL_TYPE.KEFU);
		if(kefuTel != null){
			model.setCompanyMobile(kefuTel.getData2());
		}
				
		model.setLabels(car.getLabels());
		
		model.setDescUrl(StringUtil.cutBodyHeader(car.getContent()));
		
		TCartParamsEntity params = paramsService.queryObjectByCartId(car.getId(),Constants.CAR_SALE_TYPE.LEASE);
		
		if(params != null){
			model.setCheshenjiegou(params.getCheshenjiegou());
			model.setFadongjixinghao(params.getFadongjixinghao());
			model.setQudongtype(params.getQudongtype());
			model.setHeight(params.getHeight());
			model.setWidth(params.getWidth());
			model.setLength(params.getLength());
			model.setBiansuxiangtype(params.getBiansuxiangtype());
			model.setRanliaotype(params.getRanliaotype());
			model.setZhuchezhidongtype(params.getZhuchezhidongtype());
			model.setPailiang(params+"mL");
		}
		//获取购买说明
		TCarouselEntity buyMark = carouselService.queryByTypeCd(Constants.CAROUSEL_TYPE.LEASE_CAR_BUYMARK);
		if(buyMark != null){
			model.setBuyMarkUrl(buyMark.getImgUrl());
		}
		//购买须知
		TCarouselEntity buyKnow = carouselService.queryByTypeCd(Constants.CAROUSEL_TYPE.LEASE_CAR_BUYKONW);
		if(buyKnow != null){
			model.setBuyKnowUrl(buyKnow.getImgUrl());
		}
		model.setSalecount(StringUtil.toString(car.getSalecount()));
		
		/*TCartParamsEntity params1 = paramsService.queryObjectByCartId(dto.getCartId(),Constants.CAR_SALE_TYPE.LEASE);
		TCartParam2Entity params2 = params2Service.queryObjectByCartIdType(dto.getCartId(),Constants.CAR_SALE_TYPE.LEASE);
		
		json.put("params1", params1);
		json.put("params2", params2);
		*/
		
		json.put("carDetail", model);
		data.setData(json);
		data.setCode(Constants.STATUS_CODE.SUCCESS);
		data.setMessage("查询成功");
		renderJson(data, response);
	}
	
	@RequestMapping("/queryCarParams")
	public void queryCarParams(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ParamsDTO dto = ParamsDTO.getInstance(request);
		ReturnData data = new ReturnData();
		JSONObject json = new JSONObject();
		int cartId = dto.getCartId();
		if(cartId == 0){
			data.setCode(Constants.STATUS_CODE.FAIL);
			data.setMessage("您来晚啦，此车已下架");
			renderJson(data, response);
			return;
		}
		
		TCartParamsEntity params1 = paramsService.queryObjectByCartId(cartId,dto.getTypeCd());
		TCartParam2Entity params2 = params2Service.queryObjectByCartIdType(cartId,dto.getTypeCd());
		
		json.put("params1", params1);
		json.put("params2", params2);
		data.setData(json);
		data.setCode(Constants.STATUS_CODE.SUCCESS);
		data.setMessage("查询成功");
		renderJson(data, response);
	}
	
	@RequestMapping("/queryCustomerService")
	public void queryCustomerService(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ParamsDTO dto = ParamsDTO.getInstance(request);
		ReturnData data = new ReturnData();
		JSONObject json = new JSONObject();
		json.put("address", "厦门市翔安区马巷镇巷西路611号");
		json.put("emailCode", "3610000");
		json.put("phone", "0592-6597999");
		json.put("longtitude", "118.24338197705003");
		json.put("latitude", "24.674669004378305");
		json.put("companyName", "厦门永翔群汽车贸易有限公司");
		TCarouselEntity serviceImg = carouselService.queryByTypeCd(Constants.CAROUSEL_TYPE.AFTER_SALE);
		if(serviceImg != null) {
			json.put("customerServiceImg", serviceImg.getImgUrl());
		}else {
			json.put("customerServiceImg", "");
		}
		data.setData(json);
		data.setCode(Constants.STATUS_CODE.SUCCESS);
		data.setMessage("查询成功");
		renderJson(data, response);
	}
}
