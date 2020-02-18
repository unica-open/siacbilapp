/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capentgest;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloEntrataModel;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDisponibilitaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.DisponibilitaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEGest;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.wrapper.EntitaConsultabileFilter;
/**
 * Classe di model per la consultazione del Capitolo di Entrata Gestione. Contiene una mappatura dei form per il
 * Capitolo di Entrata Gestione.
 *
 * @author Alessandro Marchino
 * @version 1.0.0 30/07/2013
 *
 */
public class ConsultaCapitoloEntrataGestioneModel extends CapitoloEntrataModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4256856520682050552L;

	/* Maschera 1 */
	private CapitoloEntrataGestione capitolo;

	private ImportiCapitoloEG importiCapitoloEntrataGestione0;
	private ImportiCapitoloEG importiCapitoloEntrataGestione1;
	private ImportiCapitoloEG importiCapitoloEntrataGestione2;
	private ImportiCapitoloEG importiEsercizioPrecedente;

	// Per il dettaglio
	private Boolean variazioneInAumento;

	// CR-4324
	private DisponibilitaCapitoloEntrataGestione disponibilitaCapitoloEntrataGestioneAnno0;
	private DisponibilitaCapitoloEntrataGestione disponibilitaCapitoloEntrataGestioneAnno1;
	private DisponibilitaCapitoloEntrataGestione disponibilitaCapitoloEntrataGestioneAnno2;
	private DisponibilitaCapitoloEntrataGestione disponibilitaCapitoloEntrataGestioneResiduo;
	// SIAC-6193
	private List<EntitaConsultabileFilter> listaFiltroAccertamento = new ArrayList<EntitaConsultabileFilter>();
	private List<EntitaConsultabileFilter> listaFiltroOrdinativo = new ArrayList<EntitaConsultabileFilter>();
	// SIAC-6305
	private String openTab;

	/** Costruttore vuoto di default */
	public ConsultaCapitoloEntrataGestioneModel() {
		super();
		setTitolo("Consultazione Capitolo Entrata Gestione");
	}

	/**
	 * @return the capitoloEntrataGestione
	 */
	public CapitoloEntrataGestione getCapitolo() {
		return capitolo;
	}

	/**
	 * @param capitolo the capitoloEntrataGestione to set
	 */
	public void setCapitolo(CapitoloEntrataGestione capitolo) {
		this.capitolo = capitolo;
	}

	/**
	 * @return the importiCapitoloEntrataGestione0
	 */
	public ImportiCapitoloEG getImportiCapitoloEntrataGestione0() {
		return importiCapitoloEntrataGestione0;
	}

	/**
	 * @param importiCapitoloEntrataGestione0 the importiCapitoloEntrataGestione0 to set
	 */
	public void setImportiCapitoloEntrataGestione0(ImportiCapitoloEG importiCapitoloEntrataGestione0) {
		this.importiCapitoloEntrataGestione0 = importiCapitoloEntrataGestione0;
	}

	/**
	 * @return the importiCapitoloEntrataGestione1
	 */
	public ImportiCapitoloEG getImportiCapitoloEntrataGestione1() {
		return importiCapitoloEntrataGestione1;
	}

	/**
	 * @param importiCapitoloEntrataGestione1 the importiCapitoloEntrataGestione1 to set
	 */
	public void setImportiCapitoloEntrataGestione1(ImportiCapitoloEG importiCapitoloEntrataGestione1) {
		this.importiCapitoloEntrataGestione1 = importiCapitoloEntrataGestione1;
	}

	/**
	 * @return the importiCapitoloEntrataGestione2
	 */
	public ImportiCapitoloEG getImportiCapitoloEntrataGestione2() {
		return importiCapitoloEntrataGestione2;
	}

	/**
	 * @param importiCapitoloEntrataGestione2 the importiCapitoloEntrataGestione2 to set
	 */
	public void setImportiCapitoloEntrataGestione2(ImportiCapitoloEG importiCapitoloEntrataGestione2) {
		this.importiCapitoloEntrataGestione2 = importiCapitoloEntrataGestione2;
	}

	/**
	 * Ottiene l'uid del capitolo da consultare.
	 *
	 * @return l'uid del Capitolo di Entrata Gestione da consultare
	 */
	public int getUidDaConsultare() {
		return capitolo == null ? 0 : capitolo.getUid();
	}

	/**
	 * Imposta l'uid nel Capitolo di Entrata Gestione da consultare.
	 *
	 * @param uidDaConsultare l'uid da impostare
	 */
	public void setUidDaConsultare(int uidDaConsultare) {
		if(capitolo == null) {
			capitolo = new CapitoloEntrataGestione();
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
	 * @return the importiEsercizioPrecedente
	 */
	public ImportiCapitoloEG getImportiEsercizioPrecedente() {
		return importiEsercizioPrecedente;
	}

	/**
	 * @param importiEsercizioPrecedente the importiEsercizioPrecedente to set
	 */
	public void setImportiEsercizioPrecedente(ImportiCapitoloEG importiEsercizioPrecedente) {
		this.importiEsercizioPrecedente = importiEsercizioPrecedente;
	}

	/**
	 * @return the disponibilitaCapitoloEntrataGestioneAnno0
	 */
	public DisponibilitaCapitoloEntrataGestione getDisponibilitaCapitoloEntrataGestioneAnno0() {
		return disponibilitaCapitoloEntrataGestioneAnno0;
	}

	/**
	 * @param disponibilitaCapitoloEntrataGestioneAnno0 the disponibilitaCapitoloEntrataGestioneAnno0 to set
	 */
	public void setDisponibilitaCapitoloEntrataGestioneAnno0(DisponibilitaCapitoloEntrataGestione disponibilitaCapitoloEntrataGestioneAnno0) {
		this.disponibilitaCapitoloEntrataGestioneAnno0 = disponibilitaCapitoloEntrataGestioneAnno0;
	}

	/**
	 * @return the disponibilitaCapitoloEntrataGestioneAnno1
	 */
	public DisponibilitaCapitoloEntrataGestione getDisponibilitaCapitoloEntrataGestioneAnno1() {
		return disponibilitaCapitoloEntrataGestioneAnno1;
	}

	/**
	 * @param disponibilitaCapitoloEntrataGestioneAnno1 the disponibilitaCapitoloEntrataGestioneAnno1 to set
	 */
	public void setDisponibilitaCapitoloEntrataGestioneAnno1(DisponibilitaCapitoloEntrataGestione disponibilitaCapitoloEntrataGestioneAnno1) {
		this.disponibilitaCapitoloEntrataGestioneAnno1 = disponibilitaCapitoloEntrataGestioneAnno1;
	}

	/**
	 * @return the disponibilitaCapitoloEntrataGestioneAnno2
	 */
	public DisponibilitaCapitoloEntrataGestione getDisponibilitaCapitoloEntrataGestioneAnno2() {
		return disponibilitaCapitoloEntrataGestioneAnno2;
	}

	/**
	 * @param disponibilitaCapitoloEntrataGestioneAnno2 the disponibilitaCapitoloEntrataGestioneAnno2 to set
	 */
	public void setDisponibilitaCapitoloEntrataGestioneAnno2(DisponibilitaCapitoloEntrataGestione disponibilitaCapitoloEntrataGestioneAnno2) {
		this.disponibilitaCapitoloEntrataGestioneAnno2 = disponibilitaCapitoloEntrataGestioneAnno2;
	}

	/**
	 * @return the disponibilitaCapitoloEntrataGestioneResiduo
	 */
	public DisponibilitaCapitoloEntrataGestione getDisponibilitaCapitoloEntrataGestioneResiduo() {
		return disponibilitaCapitoloEntrataGestioneResiduo;
	}

	/**
	 * @param disponibilitaCapitoloEntrataGestioneResiduo the disponibilitaCapitoloEntrataGestioneResiduo to set
	 */
	public void setDisponibilitaCapitoloEntrataGestioneResiduo(DisponibilitaCapitoloEntrataGestione disponibilitaCapitoloEntrataGestioneResiduo) {
		this.disponibilitaCapitoloEntrataGestioneResiduo = disponibilitaCapitoloEntrataGestioneResiduo;
	}

	/**
	 * @return the listaFiltroAccertamento
	 */
	public List<EntitaConsultabileFilter> getListaFiltroAccertamento() {
		return this.listaFiltroAccertamento;
	}

	/**
	 * @param listaFiltroAccertamento the listaFiltroAccertamento to set
	 */
	public void setListaFiltroAccertamento(List<EntitaConsultabileFilter> listaFiltroAccertamento) {
		this.listaFiltroAccertamento = listaFiltroAccertamento;
	}

	/**
	 * @return the listaFiltroOrdinativo
	 */
	public List<EntitaConsultabileFilter> getListaFiltroOrdinativo() {
		return this.listaFiltroOrdinativo;
	}

	/**
	 * @param listaFiltroOrdinativo the listaFiltroOrdinativo to set
	 */
	public void setListaFiltroOrdinativo(List<EntitaConsultabileFilter> listaFiltroOrdinativo) {
		this.listaFiltroOrdinativo = listaFiltroOrdinativo;
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
	 * Crea una Request per il servizio di {@link RicercaDettaglioCapitoloEntrataGestione}.
	 *
	 * @return la Request creata
	 */
	public RicercaDettaglioCapitoloEntrataGestione creaRequestRicercaDettaglioCapitoloEntrataGestione() {
		RicercaDettaglioCapitoloEntrataGestione request = creaRequest(RicercaDettaglioCapitoloEntrataGestione.class);
		request.setEnte(getEnte());
		request.setRicercaDettaglioCapitoloEGest(creaUtilitaRicercadettaglioCapitoloEntrataGestione(capitolo.getUid()));
		request.setImportiDerivatiRichiesti(EnumSet.allOf(ImportiCapitoloEnum.class));

		return request;
	}
	
	/**
	 * Crea una Request per il servizio di {@link RicercaDisponibilitaCapitoloEntrataGestione}.
	 * @return la request creata
	 */
	public RicercaDisponibilitaCapitoloEntrataGestione creaRequestRicercaDisponibilitaCapitoloEntrataGestione() {
		RicercaDisponibilitaCapitoloEntrataGestione req = creaRequest(RicercaDisponibilitaCapitoloEntrataGestione.class);
		
		req.setAnnoBilancio(getAnnoEsercizioInt());
		
		CapitoloEntrataGestione capitoloEntrataGestione = new CapitoloEntrataGestione();
		capitoloEntrataGestione.setUid(getCapitolo().getUid());
		req.setCapitoloEntrataGestione(capitoloEntrataGestione);
		
		return req;
	}

	/* Metodi di utilita' */

	/**
	 * Metodo di utilit&agrave; per la Ricerca Dettaglio del Capitolo di Entrata Gestione.
	 * @param chiaveCapitolo la chiave univoca del capitolo
	 *
	 * @return l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloEGest creaUtilitaRicercadettaglioCapitoloEntrataGestione(int chiaveCapitolo) {
		RicercaDettaglioCapitoloEGest utility = new RicercaDettaglioCapitoloEGest();
		utility.setChiaveCapitolo(chiaveCapitolo);
		return utility;
	}

	/**
	 * Imposta i dati nel model a partire dalla Response del servizio di {@link RicercaDettaglioCapitoloEntrataGestione} e dalla sessione.
	 *
	 * @param response la Response del servizio di ricerca dettaglio
	 */
	public void impostaDatiDaResponse(RicercaDettaglioCapitoloEntrataGestioneResponse response) {
		capitolo = response.getCapitoloEntrataGestione();

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
		impostaClassificatoriGenericiDaLista(response.getCapitoloEntrataGestione().getClassificatoriGenerici());

		impostaImportiCapitoloEntrataGestione(capitolo.getListaImportiCapitoloEG());

		controllaImportiCapitoloEquivalente(capitolo, ImportiCapitoloEG.class);
		importiEsercizioPrecedente = capitolo.getImportiCapitoloEquivalente();
	}

	/**
	 * Imposta gli importi del Capitolo di Entrata a partire dalla Response del servizio di
	 * {@link RicercaDettaglioCapitoloEntrataGestione}.
	 *
	 * @param listaImporti la lista degli importi
	 */
	private void impostaImportiCapitoloEntrataGestione(List<ImportiCapitoloEG> listaImporti) {
		Integer anno = getAnnoEsercizioInt();
		int annoInt = anno.intValue();
		try {
			importiCapitoloEntrataGestione0 = ComparatorUtils.searchByAnno(listaImporti, anno);
			importiCapitoloEntrataGestione1 = ComparatorUtils.searchByAnno(listaImporti, Integer.valueOf(annoInt + 1));
			importiCapitoloEntrataGestione2 = ComparatorUtils.searchByAnno(listaImporti, Integer.valueOf(annoInt + 2));
		} catch(Exception e) {
			// Ignoro l'errore: importi non impostabili
		}
	}

}
