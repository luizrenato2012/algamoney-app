package com.algaworks.algamoney.api.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("algamoney")
public class AlgamoneyApiProperty {
	
	private String origemPermitida="http://localhost:4200";
	
	private Seguranca seguranca;
	
	public String getOrigemPermitida() {
		return origemPermitida;
	}

	public void setOrigemPermitida(String origemPermitida) {
		this.origemPermitida = origemPermitida;
	}

	public Seguranca getSeguranca() {
		return seguranca;
	}

	public void setSeguranca(Seguranca seguranca) {
		this.seguranca = seguranca;
	}


	public static class Seguranca {
		private Boolean enableHttps;

		public Boolean getEnableHttps() {
			return enableHttps;
		}

		public void setEnableHttps(Boolean enableHttps) {
			this.enableHttps = enableHttps;
		}
		
	}

}
