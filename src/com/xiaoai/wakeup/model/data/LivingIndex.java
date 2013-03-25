package com.xiaoai.wakeup.model.data;

import org.json.JSONObject;

import com.xiaoai.wakeup.util.ParseUtil;

public class LivingIndex {

	private String date; // the date of the indexs (yyyy-MM-dd)

	private String indexDressing; // the dressing index of today

	private String indexDressingDetail; // the dressing index details of today

	private String indexUV; // the UV index of today

	private String indexCarWash; // the car wash index

	private String indexTravel; // the travel index

	private String indexComfort; // the comfortable index

	private String indexMorningExercise; // the morning exercise index

	private String indexDrying; // the drying index

	private String indexAllergy; // the allergic index

	public LivingIndex() {
		super();
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getIndexDressing() {
		return indexDressing;
	}

	public void setIndexDressing(String indexDressing) {
		this.indexDressing = indexDressing;
	}

	public String getIndexDressingDetail() {
		return indexDressingDetail;
	}

	public void setIndexDressingDetail(String indexDressingDetail) {
		this.indexDressingDetail = indexDressingDetail;
	}

	public String getIndexUV() {
		return indexUV;
	}

	public void setIndexUV(String indexUV) {
		this.indexUV = indexUV;
	}

	public String getIndexCarWash() {
		return indexCarWash;
	}

	public void setIndexCarWash(String indexCarWash) {
		this.indexCarWash = indexCarWash;
	}

	public String getIndexTravel() {
		return indexTravel;
	}

	public void setIndexTravel(String indexTravel) {
		this.indexTravel = indexTravel;
	}

	public String getIndexComfort() {
		return indexComfort;
	}

	public void setIndexComfort(String indexComfort) {
		this.indexComfort = indexComfort;
	}

	public String getIndexMorningExercise() {
		return indexMorningExercise;
	}

	public void setIndexMorningExercise(String indexMorningExercise) {
		this.indexMorningExercise = indexMorningExercise;
	}

	public String getIndexDrying() {
		return indexDrying;
	}

	public void setIndexDrying(String indexDrying) {
		this.indexDrying = indexDrying;
	}

	public String getIndexAllergy() {
		return indexAllergy;
	}

	public void setIndexAllergy(String indexAllergy) {
		this.indexAllergy = indexAllergy;
	}

	@Override
	public String toString() {
		return "LivingIndex [date=" + date + ", indexDressing=" + indexDressing
				+ ", indexDressingDetail=" + indexDressingDetail + ", indexUV="
				+ indexUV + ", indexCarWash=" + indexCarWash + ", indexTravel="
				+ indexTravel + ", indexComfort=" + indexComfort
				+ ", indexMorningExercise=" + indexMorningExercise
				+ ", indexDrying=" + indexDrying + ", indexAllergy="
				+ indexAllergy + "]";
	}

	public static LivingIndex generateFromJson(JSONObject object) {
		if (object != null) {
			LivingIndex index = new LivingIndex();
			index.indexDressing = ParseUtil.parseString(object, "index");
			index.indexDressingDetail = ParseUtil
					.parseString(object, "index_d");
			index.indexUV = ParseUtil.parseString(object, "index_uv");
			index.indexCarWash = ParseUtil.parseString(object, "index_xc");
			index.indexTravel = ParseUtil.parseString(object, "index_tr");
			index.indexComfort = ParseUtil.parseString(object, "index_co");
			index.indexMorningExercise = ParseUtil.parseString(object,
					"index_cl");
			index.indexDrying = ParseUtil.parseString(object, "index_ls");
			index.indexAllergy = ParseUtil.parseString(object, "index_ag");
			return index;
		}
		return null;
	}

}
