package jlearning.bean;

import java.util.List;

public class LessonInfo {
	private String name;
	private String description;
	private List<VocabInfo> vocabs;
	public List<VocabInfo> getVocabs() {
		return vocabs;
	}
	public void setVocabs(List<VocabInfo> vocabs) {
		this.vocabs = vocabs;
	}
	public List<GramInfo> getGrams() {
		return grams;
	}
	public void setGrams(List<GramInfo> grams) {
		this.grams = grams;
	}
	public List<ListenInfo> getListens() {
		return listens;
	}
	public void setListens(List<ListenInfo> listens) {
		this.listens = listens;
	}
	private List<GramInfo> grams;
	private List<ListenInfo> listens;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	

}
