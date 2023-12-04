/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.pagamento;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseConsultaRichiestaEconomaleModel;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;


/**
 * Classe di model per la consultazione del pagamento
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 28/01/2016
 */
public class ConsultaPagamentoCassaEconomaleModel extends BaseConsultaRichiestaEconomaleModel {
	

	/** Per la serializzazione */
	private static final long serialVersionUID = 3734224877809805503L;

	/** Costruttore vuoto di default */
	public ConsultaPagamentoCassaEconomaleModel() {
		setTitolo("Consultazione pagamento");
	}
	
	/**
	 * @return the descrizioneFatturaRiferimentoPerRiepilogo
	 */
	public String getDescrizioneFatturaRiferimentoPerRiepilogo(){
		final StringBuilder sb = new StringBuilder();
		if (getRichiestaEconomale()!= null 
				&& getRichiestaEconomale().getSubdocumenti()!= null 
				&& !getRichiestaEconomale().getSubdocumenti().isEmpty()){
			//recupero i dati del padre
			DocumentoSpesa fatPadre = getRichiestaEconomale().getSubdocumenti().get(0).getDocumento();
			
			sb.append("Anno ");
			sb.append(fatPadre.getAnno());
			sb.append(" Numero  ");
			sb.append(fatPadre.getNumero());
			sb.append(" Quota ");
			sb.append(getRichiestaEconomale().getSubdocumenti().get(0).getNumero());
			sb.append(" Soggetto ");
			sb.append(fatPadre.getSoggetto().getDenominazione());
			
			
			}
		return sb.toString();
	}
	
	/**
	 * @return the datiSoggetto
	 */
	public String getDatiSoggetto() {
		
		if(getRichiestaEconomale().getSoggetto() != null) {
			List<String> chunks = new ArrayList<String>();
			CollectionUtil.addIfNotNullNorEmpty(chunks, getRichiestaEconomale().getSoggetto().getCodiceSoggetto());
			CollectionUtil.addIfNotNullNorEmpty(chunks, getRichiestaEconomale().getSoggetto().getDenominazione());
			return StringUtils.join(chunks, " - ");
		}
		List<String> chunks = new ArrayList<String>();
		//CollectionUtil.addIfNotNullNorEmpty(chunks, getRichiestaEconomale().getCodiceBeneficiario());
		CollectionUtil.addIfNotNullNorEmpty(chunks, getRichiestaEconomale().getNome());
		CollectionUtil.addIfNotNullNorEmpty(chunks, getRichiestaEconomale().getCognome());
		return StringUtils.join(chunks, " - ");
	}

}
