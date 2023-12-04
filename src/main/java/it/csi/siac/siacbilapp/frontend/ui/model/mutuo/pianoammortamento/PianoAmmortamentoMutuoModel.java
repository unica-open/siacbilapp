/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.mutuo.pianoammortamento;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.base.BaseMutuoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AggiornaPianoAmmortamentoMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AnnullaPianoAmmortamentoMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaDettaglioMutuo;
import it.csi.siac.siacbilser.model.mutuo.MutuoModelDetail;
import it.csi.siac.siacbilser.model.mutuo.StatoMutuo;
import it.csi.siac.siaccommon.util.collections.ArrayUtil;
import it.csi.siac.siaccommon.util.collections.Function;
import it.csi.siac.siaccommon.util.date.DateUtil;
import it.csi.siac.siaccommon.util.number.BigDecimalUtil;

public class PianoAmmortamentoMutuoModel extends BaseMutuoModel {

	private File fileElencoRate;
	private String dataScadenzaStr;
	private String importoTotaleStr;
	private String importoQuotaCapitaleStr;
	private String importoQuotaInteressiStr;
	private String importoQuotaOneriStr;
	
	private Date[] dataScadenza;
	private BigDecimal[] importoTotale;
	private BigDecimal[] importoQuotaCapitale;
	private BigDecimal[] importoQuotaInteressi;
	private BigDecimal[] importoQuotaOneri;
	
	
	private static final MutuoModelDetail[] DETTAGLIO_MUTUO_MODEL_DETAILS = 
		ArrayUtil.concat(MutuoModelDetail.class,
			MutuoModelDetail.PianoAmmortamentoConTotali, 
			MutuoModelDetail.Stato, 
			MutuoModelDetail.Soggetto, 
			MutuoModelDetail.PeriodoRimborso
		);

	private static final long serialVersionUID = -7315689113187835663L;

	public PianoAmmortamentoMutuoModel() {
		super();
		setTitolo("Piano Ammortamento Mutuo");
	}
	
	@Override
	public RicercaDettaglioMutuo creaRequestRicercaDettaglioMutuo() {
		return creaRequestRicercaDettaglioMutuoWithModelDetails(DETTAGLIO_MUTUO_MODEL_DETAILS);
	}
	
	public AnnullaPianoAmmortamentoMutuo creaRequestAnnullaPianoAmmortamentoMutuo() {
		AnnullaPianoAmmortamentoMutuo request = creaRequest(AnnullaPianoAmmortamentoMutuo.class);
		
		request.setMutuo(getMutuo());
		
		return request;
	}
	
	public AggiornaPianoAmmortamentoMutuo creaRequestAggiornaPianoAmmortamentoMutuo(StatoMutuo statoMutuo) {
		AggiornaPianoAmmortamentoMutuo request = creaRequest(AggiornaPianoAmmortamentoMutuo.class);
		
		request.setMutuo(getMutuo());
		request.setStatoMutuo(statoMutuo);
		
		return request;
	}

	public File getFileElencoRate() {
		return fileElencoRate;
	}

	public void setFileElencoRate(File fileElencoRate) {
		this.fileElencoRate = fileElencoRate;
	}

	public Date[] getDataScadenza() {
		if (dataScadenza == null) {
			dataScadenza = ArrayUtil.map(splitStr(dataScadenzaStr), Date.class, new Function<String, Date>() {

				@Override
				public Date map(String source) {
					return DateUtil.parseDate(source);
				}});
		}
		
		return dataScadenza;
	}

	public BigDecimal[] getImportoTotale() {
		return (importoTotale != null) ? importoTotale : (importoTotale = strToBigDecimalArray(importoTotaleStr));
	}

	public BigDecimal[] getImportoQuotaCapitale() {
		return (importoQuotaCapitale != null) ? importoQuotaCapitale : (importoQuotaCapitale = strToBigDecimalArray(importoQuotaCapitaleStr));
	}

	public BigDecimal[] getImportoQuotaInteressi() {
		return (importoQuotaInteressi != null) ? importoQuotaInteressi : (importoQuotaInteressi = strToBigDecimalArray(importoQuotaInteressiStr));
	}

	public BigDecimal[] getImportoQuotaOneri() {
		return (importoQuotaOneri != null) ? importoQuotaOneri : (importoQuotaOneri = strToBigDecimalArray(importoQuotaOneriStr));
	}

	private BigDecimal[] strToBigDecimalArray(String str) {
		return StringUtils.isBlank(str) ?
				new BigDecimal[] { null } :
				ArrayUtil.map(splitStr(str), BigDecimal.class, new Function<String, BigDecimal>() {

		@Override
		public BigDecimal map(String source) {
			return BigDecimalUtil.parseBigDecimal(source);
		}});
	}

	private String[] splitStr(String str) {
		return StringUtils.splitPreserveAllTokens(str, ":");
	}

	public String getDataScadenzaStr() {
		return dataScadenzaStr;
	}

	public void setDataScadenzaStr(String dataScadenzaStr) {
		this.dataScadenzaStr = dataScadenzaStr;
	}

	public String getImportoQuotaCapitaleStr() {
		return importoQuotaCapitaleStr;
	}

	public void setImportoQuotaCapitaleStr(String importoQuotaCapitaleStr) {
		this.importoQuotaCapitaleStr = importoQuotaCapitaleStr;
	}

	public String getImportoQuotaInteressiStr() {
		return importoQuotaInteressiStr;
	}

	public void setImportoQuotaInteressiStr(String importoQuotaInteressiStr) {
		this.importoQuotaInteressiStr = importoQuotaInteressiStr;
	}

	public String getImportoQuotaOneriStr() {
		return importoQuotaOneriStr;
	}

	public void setImportoQuotaOneriStr(String importoQuotaOneriStr) {
		this.importoQuotaOneriStr = importoQuotaOneriStr;
	}

	public String getImportoTotaleStr() {
		return importoTotaleStr;
	}

	public void setImportoTotaleStr(String importoTotaleStr) {
		this.importoTotaleStr = importoTotaleStr;
	}
	

}
