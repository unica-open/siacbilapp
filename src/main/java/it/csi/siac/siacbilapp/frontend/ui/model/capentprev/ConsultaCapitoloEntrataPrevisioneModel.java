/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capentprev;

import java.util.EnumSet;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloEntrataPrevisioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.ImportiCapitoloEP;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEPrev;

/**
 * Classe di model per la consultazione del Capitolo di Entrata Previsione. Contiene una mappatura dei form per il
 * Capitolo di Entrata Previsione.
 *
 * @author Alessandro Marchino
 * @version 1.0.0 02/08/2013
 *
 */
public class ConsultaCapitoloEntrataPrevisioneModel extends CapitoloEntrataPrevisioneModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -2807681321598816389L;

	/* Maschera 1 */
	private CapitoloEntrataPrevisione capitolo;

	private ImportiCapitoloEG importiEx;
	private ImportiCapitoloEP importiCapitoloEntrataPrevisione0;
	private ImportiCapitoloEP importiCapitoloEntrataPrevisione1;
	private ImportiCapitoloEP importiCapitoloEntrataPrevisione2;

	// Per il dettaglio
	private Boolean variazioneInAumento;
	// SIAC-6305
	private String openTab;

	/** Costruttore vuoto di default */
	public ConsultaCapitoloEntrataPrevisioneModel() {
		super();
		setTitolo("Consultazione Capitolo Entrata Previsione");
	}

	/* Getter e setter */

	/**
	 * @return the capitoloEntrataPrevisione
	 */
	public CapitoloEntrataPrevisione getCapitolo() {
		return capitolo;
	}

	/**
	 * @param capitolo the capitoloEntrataPrevisione to set
	 */
	public void setCapitolo(CapitoloEntrataPrevisione capitolo) {
		this.capitolo = capitolo;
	}

	/**
	 * @return the importiEx
	 */
	public ImportiCapitoloEG getImportiEx() {
		return importiEx;
	}

	/**
	 * @param importiEx the importiEx to set
	 */
	public void setImportiEx(ImportiCapitoloEG importiEx) {
		this.importiEx = importiEx;
	}

	/**
	 * @return the importiCapitoloEntrataPrevisione0
	 */
	public ImportiCapitoloEP getImportiCapitoloEntrataPrevisione0() {
		return importiCapitoloEntrataPrevisione0;
	}

	/**
	 * @param importiCapitoloEntrataPrevisione0 the importiCapitoloEntrataPrevisione0 to set
	 */
	public void setImportiCapitoloEntrataPrevisione0(ImportiCapitoloEP importiCapitoloEntrataPrevisione0) {
		this.importiCapitoloEntrataPrevisione0 = importiCapitoloEntrataPrevisione0;
	}

	/**
	 * @return the importiCapitoloEntrataPrevisione1
	 */
	public ImportiCapitoloEP getImportiCapitoloEntrataPrevisione1() {
		return importiCapitoloEntrataPrevisione1;
	}

	/**
	 * @param importiCapitoloEntrataPrevisione1 the importiCapitoloEntrataPrevisione1 to set
	 */
	public void setImportiCapitoloEntrataPrevisione1(ImportiCapitoloEP importiCapitoloEntrataPrevisione1) {
		this.importiCapitoloEntrataPrevisione1 = importiCapitoloEntrataPrevisione1;
	}

	/**
	 * @return the importiCapitoloEntrataPrevisione2
	 */
	public ImportiCapitoloEP getImportiCapitoloEntrataPrevisione2() {
		return importiCapitoloEntrataPrevisione2;
	}

	/**
	 * @param importiCapitoloEntrataPrevisione2 the importiCapitoloEntrataPrevisione2 to set
	 */
	public void setImportiCapitoloEntrataPrevisione2(ImportiCapitoloEP importiCapitoloEntrataPrevisione2) {
		this.importiCapitoloEntrataPrevisione2 = importiCapitoloEntrataPrevisione2;
	}

	/**
	 * Ottiene l'uid del capitolo da consultare.
	 *
	 * @return l'uid del Capitolo di Entrata Previsione da consultare
	 */
	public int getUidDaConsultare() {
		return capitolo == null ? 0 : capitolo.getUid();
	}

	/**
	 * Imposta l'uid nel Capitolo di Entrata Previsione da consultare.
	 *
	 * @param uidDaConsultare l'uid da impostare
	 */
	public void setUidDaConsultare(int uidDaConsultare) {
		if(capitolo == null) {
			capitolo = new CapitoloEntrataPrevisione();
		}
		capitolo.setUid(uidDaConsultare);
	}

	/**
	 * @return the variazioneInAumento
	 */
	public Boolean getVariazioneInAumento() {
		return variazioneInAumento;
	}

	/**
	 * @param variazioneInAumento the variazioneInAumento to set
	 */
	public void setVariazioneInAumento(Boolean variazioneInAumento) {
		this.variazioneInAumento = variazioneInAumento;
	}

	/**
	 * @return the openTab
	 */
	public String getOpenTab() {
		return this.openTab;
	}

	/**
	 * @param openTab the openTab to set
	 */
	public void setOpenTab(String openTab) {
		this.openTab = openTab;
	}
	
	/* Request */

	/**
	 * Crea una Request per il servizio di {@link RicercaDettaglioCapitoloEntrataPrevisione}.
	 *
	 * @return la Request creata
	 */
	public RicercaDettaglioCapitoloEntrataPrevisione creaRequestRicercaDettaglioCapitoloEntrataPrevisione() {
		RicercaDettaglioCapitoloEntrataPrevisione request = creaRequest(RicercaDettaglioCapitoloEntrataPrevisione.class);
		request.setEnte(getEnte());
		request.setRicercaDettaglioCapitoloEPrev(creaUtilitaRicercadettaglioCapitoloEntrataPrevisione(capitolo.getUid()));
		request.setImportiDerivatiRichiesti(EnumSet.allOf(ImportiCapitoloEnum.class));
		return request;
	}

	/* Metodi di utilita' */

	/**
	 * Metodo di utilit&agrave; per la Ricerca Dettaglio del Capitolo di Entrata Previsione.
	 * @param chiaveCapitolo la chiave univoca del capitolo
	 *
	 * @return l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloEPrev creaUtilitaRicercadettaglioCapitoloEntrataPrevisione(int chiaveCapitolo) {
		RicercaDettaglioCapitoloEPrev utility = new RicercaDettaglioCapitoloEPrev();
		utility.setChiaveCapitolo(chiaveCapitolo);
		return utility;
	}

	/**
	 * Imposta i dati nel model a partire dalla Response del servizio di {@link RicercaDettaglioCapitoloEntrataPrevisione} e dalla sessione.
	 *
	 * @param response la Response del servizio di ricerca dettaglio
	 */
	public void impostaDatiDaResponse(RicercaDettaglioCapitoloEntrataPrevisioneResponse response) {
		capitolo = response.getCapitoloEntrataPrevisione();

		setBilancio(capitolo.getBilancio());
		setTipoFinanziamento(capitolo.getTipoFinanziamento());
		setTipoFondo(capitolo.getTipoFondo());
		setCategoriaTipologiaTitolo(capitolo.getCategoriaTipologiaTitolo());
		setElementoPianoDeiConti(capitolo.getElementoPianoDeiConti());
		setStrutturaAmministrativoContabile(capitolo.getStrutturaAmministrativoContabile());
		setTipologiaTitolo(capitolo.getTipologiaTitolo());
		setTitoloEntrata(capitolo.getTitoloEntrata());

		setSiopeEntrata(capitolo.getSiopeEntrata());
		setRicorrenteEntrata(capitolo.getRicorrenteEntrata());
		setPerimetroSanitarioEntrata(capitolo.getPerimetroSanitarioEntrata());
		setTransazioneUnioneEuropeaEntrata(capitolo.getTransazioneUnioneEuropeaEntrata());

		/* Ottenuti dalla sessione */
		impostaClassificatoriGenericiDaLista(response.getCapitoloEntrataPrevisione().getClassificatoriGenerici());

		impostaImportiCapitoloEntrataPrevisione(capitolo.getListaImportiCapitoloEP());
		impostaImportoEx(capitolo);

		controllaImportiCapitoloEquivalente(capitolo, ImportiCapitoloEG.class);
	}

	/**
	 * Imposta l'importo per i campi relativi all'ex.
	 *
	 * @param capitolo il capitolo da cui ottenere il dato
	 */
	private void impostaImportoEx(CapitoloEntrataPrevisione capitolo) {
		importiEx = capitolo.getImportiCapitoloEG();
	}

	/**
	 * Imposta gli importi del Capitolo di Entrata a partire dalla Response del servizio di
	 * {@link RicercaDettaglioCapitoloEntrataPrevisione}.
	 *
	 * @param listaImporti la lista degli importi
	 */
	private void impostaImportiCapitoloEntrataPrevisione(List<ImportiCapitoloEP> listaImporti) {
		Integer anno = getAnnoEsercizioInt();
		try {
			int annoInt = anno.intValue();
			importiCapitoloEntrataPrevisione0 = ComparatorUtils.searchByAnno(listaImporti, anno);
			importiCapitoloEntrataPrevisione1 = ComparatorUtils.searchByAnno(listaImporti, Integer.valueOf(annoInt + 1));
			importiCapitoloEntrataPrevisione2 = ComparatorUtils.searchByAnno(listaImporti, Integer.valueOf(annoInt + 2));
		} catch(Exception e) {
			// Ignoro l'errore: ipmorti non impostabili
		}
	}
	
}
