/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacbilapp.frontend.ui.model.previmpacc;

import java.util.EnumSet;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUGest;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;

/**
 * The Class RicercaPrevisioneImpegnatoAccertatoModel.
 *
 * @author elisa
 * @version 1.0.0 15 ott 2021
 */
public class RicercaPrevisioneImpegnatoAccertatoModel extends GenericBilancioModel {

	/** per la serializzazione */
	private static final long serialVersionUID = 1L;
	
	private Capitolo<?, ?> capitolo;
	
	private String nomeAzioneSAC;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	private String strutturaAmministrativoResponsabile;
	private String codiceTipoClassificatoreStrutturaAmministrativoContabile;
	
	private boolean daConsultaEntrata;
	private boolean daConsultaSpesa;
	
	private int savedDisplayStart;
	
	/** Costruttore vuoto di default */
	public RicercaPrevisioneImpegnatoAccertatoModel() {
		super();
		setTitolo("Previsione impegnato/accertato");
	}
	
/** =============================================== GETTER & SETTER ========================================== */
	
	/**
	 * @return the capitolo
	 */
	public Capitolo<?, ?> getCapitolo() {
		return capitolo;
	}

	/**
	 * @param capitolo the capitolo to set
	 */
	public void setCapitolo(Capitolo<?, ?> capitolo) {
		this.capitolo = capitolo;
	}

	/**
	 * @return the codiceTipoClassificatoreStrutturaAmministrativoContabile
	 */
	public String getCodiceTipoClassificatoreStrutturaAmministrativoContabile() {
		return codiceTipoClassificatoreStrutturaAmministrativoContabile;
	}

	/**
	 * @param codiceTipoClassificatoreStrutturaAmministrativoContabile the codiceTipoClassificatoreStrutturaAmministrativoContabile to set
	 */
	public void setCodiceTipoClassificatoreStrutturaAmministrativoContabile(
			String codiceTipoClassificatoreStrutturaAmministrativoContabile) {
		this.codiceTipoClassificatoreStrutturaAmministrativoContabile = codiceTipoClassificatoreStrutturaAmministrativoContabile;
	}
	
	public boolean isDaConsultaEntrata() {
		return daConsultaEntrata;
	}

	public void setDaConsultaEntrata(boolean daConsultaEntrata) {
		this.daConsultaEntrata = daConsultaEntrata;
	}

	public boolean isDaConsultaSpesa() {
		return daConsultaSpesa;
	}

	public void setDaConsultaSpesa(boolean daConsultaSpesa) {
		this.daConsultaSpesa = daConsultaSpesa;
	}

	/**
	 * @return the nomeAzioneSAC
	 */
	public String getNomeAzioneSAC() {
		return nomeAzioneSAC;
	}

	/**
	 * @param nomeAzioneSAC the nomeAzioneSAC to set
	 */
	public void setNomeAzioneSAC(String nomeAzioneSAC) {
		this.nomeAzioneSAC = nomeAzioneSAC;
	}

	public StrutturaAmministrativoContabile getStrutturaAmministrativoContabile() {
		return strutturaAmministrativoContabile;
	}

	public void setStrutturaAmministrativoContabile(StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
		this.strutturaAmministrativoContabile = strutturaAmministrativoContabile;
	}

	public String getStrutturaAmministrativoResponsabile() {
		return strutturaAmministrativoResponsabile;
	}

	public void setStrutturaAmministrativoResponsabile(String strutturaAmministrativoResponsabile) {
		this.strutturaAmministrativoResponsabile = strutturaAmministrativoResponsabile;
	}
	
	public boolean isForzaRicerca() {
		return daConsultaEntrata || daConsultaSpesa;
	}
	
	public int getSavedDisplayStart() {
		return savedDisplayStart;
	}


	/**
	 * @param savedDisplayStart the savedDisplayStart to set
	 */
	public void setSavedDisplayStart(int savedDisplayStart) {
		this.savedDisplayStart = savedDisplayStart;
	}


	
	/**
	 * Restituisce una Request di tipo {@link RicercaSinteticaCapitoloUscitaGestione} a partire dal Model.
	 * 
	 * @return la Request creata
	 * @throws IllegalArgumentException nel caso di errore nell'injezione dei parametri
	 */
	public RicercaSinteticaCapitoloUscitaGestione creaRequestRicercaSinteticaCapitoloUscitaGestione() {
		RicercaSinteticaCapitoloUscitaGestione request = creaRequest(RicercaSinteticaCapitoloUscitaGestione.class);
		request.setEnte(getEnte());
		
		// Gestione delle due possibilità di ricerca
		RicercaSinteticaCapitoloUGest ricercaSinteticaCapitoloUGest = creaRicercaSinteticaCapitoloUGest();
		request.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		request.setParametriPaginazione(creaParametriPaginazione());
		
		request.setTipologieClassificatoriRichiesti(
				TipologiaClassificatore.PROGRAMMA, TipologiaClassificatore.MACROAGGREGATO, TipologiaClassificatore.PDC, TipologiaClassificatore.PDC_I,
				TipologiaClassificatore.PDC_II, TipologiaClassificatore.PDC_III, TipologiaClassificatore.PDC_IV, TipologiaClassificatore.PDC_V, 
				TipologiaClassificatore.CDC, TipologiaClassificatore.CDR
				);
		
		request.setRicercaSinteticaCapitoloUGest(ricercaSinteticaCapitoloUGest);
		request.setCalcolaTotaleImporti(Boolean.TRUE);

		return request;
	}

	/**
	 * Restituisce una Request di tipo {@link RicercaSinteticaCapitoloEntrataGestione} a partire dal Model.
	 * 
	 * @return la Request creata
	 * @throws IllegalArgumentException nel caso di errore nell'injezione dei parametri
	 */
	public RicercaSinteticaCapitoloEntrataGestione creaRequestRicercaSinteticaCapitoloEntrataGestione() {
		RicercaSinteticaCapitoloEntrataGestione request = creaRequest(RicercaSinteticaCapitoloEntrataGestione.class);
		request.setEnte(getEnte());
		
		// Gestione delle due possibilità di ricerca
		RicercaSinteticaCapitoloEGest ricercaSinteticaCapitoloUGest = creaRicercaSinteticaCapitoloEGest();
		request.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		request.setParametriPaginazione(creaParametriPaginazione());
		
		request.setTipologieClassificatoriRichiesti(TipologiaClassificatore.CATEGORIA, TipologiaClassificatore.PDC, TipologiaClassificatore.PDC_I, TipologiaClassificatore.PDC_II,
				TipologiaClassificatore.PDC_III, TipologiaClassificatore.PDC_IV, TipologiaClassificatore.PDC_V, TipologiaClassificatore.CDC, TipologiaClassificatore.CDR
				);
		
		request.setRicercaSinteticaCapitoloEntrata(ricercaSinteticaCapitoloUGest);
		request.setCalcolaTotaleImporti(Boolean.TRUE);
		
		return request;
	}

	/**
	 * Crea un'Utilit&agrave; per la Ricerca Sintetica del Capitolo di Uscita Gestione.
	 * 
	 * @return l'utilit&agrave; creata
	 * 
	 * @throws IllegalArgumentException nel caso di errore nell'injezione dei parametri
	 */
	private RicercaSinteticaCapitoloUGest creaRicercaSinteticaCapitoloUGest() {
		RicercaSinteticaCapitoloUGest utility = new RicercaSinteticaCapitoloUGest();
		
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setNumeroCapitolo(capitolo.getNumeroCapitolo());
		utility.setNumeroArticolo(capitolo.getNumeroArticolo());
		utility.setCapitoliIndicatiPerPrevisioneImpegnato(Boolean.TRUE);
		//utility.setCodiceStrutturaAmmCont("");
		//utility.setCodiceTipoStrutturaAmmCont("");
		StrutturaAmministrativoContabile sac = getStrutturaAmministrativoContabile();
		if(sac != null && sac.getUid() != 0 && !StringUtils.isBlank(getCodiceTipoClassificatoreStrutturaAmministrativoContabile())) {
			utility.setCodiceStrutturaAmmCont(sac.getCodice());
			utility.setCodiceTipoStrutturaAmmCont(getCodiceTipoClassificatoreStrutturaAmministrativoContabile());
		}
		
		return utility;
	}

	/**
	 * Crea un'Utilit&agrave; per la Ricerca Sintetica del Capitolo di Entrata Gestione.
	 * 
	 * @return l'utilit&agrave; creata
	 * 
	 * @throws IllegalArgumentException nel caso di errore nell'injezione dei parametri
	 */
	private RicercaSinteticaCapitoloEGest creaRicercaSinteticaCapitoloEGest() {
		RicercaSinteticaCapitoloEGest utility = new RicercaSinteticaCapitoloEGest();
		
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setNumeroCapitolo(capitolo.getNumeroCapitolo());
		utility.setNumeroArticolo(capitolo.getNumeroArticolo());
		utility.setCapitoliIndicatiPerPrevisioneImpegnato(Boolean.TRUE);
		StrutturaAmministrativoContabile sac = getStrutturaAmministrativoContabile();
		if(sac != null && sac.getUid() != 0 && !StringUtils.isBlank(getCodiceTipoClassificatoreStrutturaAmministrativoContabile())) {
			utility.setCodiceStrutturaAmmCont(sac.getCodice());
			utility.setCodiceTipoStrutturaAmmCont(getCodiceTipoClassificatoreStrutturaAmministrativoContabile());
		}
		
		return utility;
	}
	
}
