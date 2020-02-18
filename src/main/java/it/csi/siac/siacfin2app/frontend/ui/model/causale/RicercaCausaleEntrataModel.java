/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.causale;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleEntrata;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoCausale;
import it.csi.siac.siacfin2ser.model.TipoCausale;

/**
 * Classe di model per la ricerca della Causale di entrata.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 12/05/2014
 *
 */
public class RicercaCausaleEntrataModel extends GenericCausaleEntrataModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5729888524977384863L;

	private StatoOperativoCausale statoOperativoCausale;
	
	/** Costruttore vuoto di default */
	public RicercaCausaleEntrataModel() {
		super();
		setTitolo("Ricerca causale incasso");
	}
	
	/**
	 * @return the statoOperativoCausale
	 */
	public StatoOperativoCausale getStatoOperativoCausale() {
		return statoOperativoCausale;
	}

	/**
	 * @param statoOperativoCausale the statoOperativoCausale to set
	 */
	public void setStatoOperativoCausale(StatoOperativoCausale statoOperativoCausale) {
		this.statoOperativoCausale = statoOperativoCausale;
	}
	
	/* Request */
	
	@Override
	public RicercaSinteticaCausaleEntrata creaRequestRicercaSinteticaCausaleEntrata() {
		RicercaSinteticaCausaleEntrata request = new RicercaSinteticaCausaleEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		CausaleEntrata causale = getCausale();
		if(causale == null) {
			// Possibile. Infatti, all'interno della JSP non vi sono riferimenti all'oggetto causale.
			causale = new CausaleEntrata();
		}
		
		causale.setEnte(getEnte());
		causale.setTipoCausale(getTipoCausale());
		causale.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));
		causale.setCapitoloEntrataGestione(impostaEntitaFacoltativa(getCapitolo()));
		causale.setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		causale.setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
		causale.setAccertamento(impostaEntitaFacoltativa(getMovimentoGestione()));
		causale.setSubAccertamento(impostaEntitaFacoltativa(getSubMovimentoGestione()));
		causale.setStatoOperativoCausale(getStatoOperativoCausale());
		
		request.setCausaleEntrata(causale);
		
		request.setParametriPaginazione(creaParametriPaginazione());
		return request;
	}
		
	/* Utility */
	
	/**
	 * Compone la stringa di riepilogo per la ricerca.
	 * 
	 * @param listaStrutturaAmministrativoContabile la lista delle Strutture Amministrativo Contabili
	 * 
	 * @return la stringa riepilogativa
	 */
	public String componiStringaRiepilogo(List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile) {
		StringBuilder sb = new StringBuilder();
		
		if(getStrutturaAmministrativoContabile() != null && getStrutturaAmministrativoContabile().getUid() != 0) {
			StrutturaAmministrativoContabile sac = ComparatorUtils.searchByUidWithChildren(listaStrutturaAmministrativoContabile, getStrutturaAmministrativoContabile());
			sb.append("Struttura Amministrativa: ").append(sac.getCodice()).append(" - ");
		}
		if(getTipoCausale() != null && getTipoCausale().getUid() != 0) {
			TipoCausale tipoCausale = ComparatorUtils.searchByUid(getListaTipoCausale(), getTipoCausale());
			sb.append("Tipo causale: ").append(tipoCausale.getCodice()).append(" - ");
		}
		if(getCausale() != null && stringaValorizzata(getCausale().getCodice())) {
			sb.append("Codice causale: ").append(getCausale().getCodice()).append(" - ");
		}
		if(getCausale() != null && stringaValorizzata(getCausale().getDescrizione())) {
			sb.append("Descrizione causale: ").append(getCausale().getDescrizione()).append(" - ");
		}
		if(getStatoOperativoCausale() != null){
			sb.append("Stato operativo: ").append(getStatoOperativoCausale().getDescrizione()).append(" - ");
		}
		sb.append(componiStringaCapitolo(getCapitolo()));
		sb.append(componiStringaMovimentoGestione(BilConstants.ACCERTAMENTO, getMovimentoGestione(), getSubMovimentoGestione()));
		sb.append(componiStringaSoggetto(getSoggetto(), null));
		sb.append(componiStringaAttoAmministrativo(getAttoAmministrativo(), getTipoAtto(), getStrutturaAmministrativoContabileAttoAmministrativo(),
				null, listaStrutturaAmministrativoContabile, getListaTipoAtto()));
		
		return StringUtils.substringBeforeLast(sb.toString(), "-");
	}
	
}
