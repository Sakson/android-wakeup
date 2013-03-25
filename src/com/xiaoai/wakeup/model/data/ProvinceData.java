package com.xiaoai.wakeup.model.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xiaoai.wakeup.util.ParseUtil;

public class ProvinceData {

	private String provinceName;

	private List<CityData> citys;

	public ProvinceData() {
		super();
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public List<CityData> getCitys() {
		return citys;
	}

	public void setCitys(List<CityData> citys) {
		this.citys = citys;
	}

	@Override
	public String toString() {
		return "Province [provinceName=" + provinceName + ", citys=" + citys
				+ "]";
	}

	public static ProvinceData generateFromJson(JSONObject object) {
		if (object != null) {
			ProvinceData province = new ProvinceData();
			province.provinceName = ParseUtil.parseString(object, "province");
			JSONArray array = ParseUtil.parseJSONArray(object, "citys");
			province.citys = CityData.generateArrayFromJson(array);
			return province;
		}
		return null;
	}

	public static List<ProvinceData> generateArrayFromJson(JSONArray array) {
		List<ProvinceData> provinces = new ArrayList<ProvinceData>();
		if (array != null) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = ParseUtil.parseJSONObject(array, i);
				ProvinceData province = generateFromJson(object);
				if (province != null) {
					provinces.add(province);
				}
			}
		}
		return provinces;
	}

}
