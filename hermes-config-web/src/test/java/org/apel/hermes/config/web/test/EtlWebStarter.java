package org.apel.hermes.config.web.test;

import java.util.TimeZone;

import org.apel.gaia.container.boot.PlatformStarter;

public class EtlWebStarter {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		PlatformStarter.start();
	}
}
