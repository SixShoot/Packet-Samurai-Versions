package com.aionemu.packetsamurai.utils.collector.data.houses;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "SpawnType")
@XmlEnum
public enum SpawnType {

	MANAGER,
	TELEPORT,
	SIGN;

	public String value() {
		return name();
	}

	public static SpawnType fromValue(String v) {
		return valueOf(v);
	}

}
