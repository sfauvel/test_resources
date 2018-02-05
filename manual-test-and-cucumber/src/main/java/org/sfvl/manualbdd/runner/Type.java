package org.sfvl.manualbdd.runner;

public enum Type {
	Ok("@ok"), Ko("@ko"), Pending("~@ok", "~@ko");

	String[] tags;

	Type(String... tags) {
		this.tags = tags;
	}
}