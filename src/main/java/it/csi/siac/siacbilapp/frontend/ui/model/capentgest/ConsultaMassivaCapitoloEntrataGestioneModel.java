/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capentgest;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloEntrataModel;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitoloFactory;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloMassivaEntrataGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEGest;
import it.csi.siac.siaccommonapp.handler.session.SessionHandler;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;

/**
 * Classe di model per la consultazione massiva del Capitolo di Entrata Gestione. Contiene una mappatura dei form per il
 * Capitolo di Entrata Gestione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 23/09/2013
 *
 */
public class ConsultaMassivaCapitoloEntrataGestioneModel extends CapitoloEntrataModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7468063563092832776L;
	
	/* Maschera 1 */
	private CapitoloEntrataGestione capitoloEntrataGestione;
	
	private ImportiCapitoloEG importiCapitoloEntrataGestioneM1;
	private ImportiCapitoloEG importiCapitoloEntrataGestione0;
	private ImportiCapitoloEG importiCapitoloEntrataGestione1;
	private ImportiCapitoloEG importiCapitoloEntrataGestione2;
	
	/* Maschera 2: UEB collegate */
	private List<ElementoCapitolo> listaUEBCollegate = new ArrayList<ElementoCapitolo>();
	
	/** Costruttore vuoto di default */
	public ConsultaMassivaCapitoloEntrataGestioneModel() {
		super();
		setTitolo("Consulta Capitolo Entrata Gestione (Massivo)");
	}
	
	/* Getter e Setter */

	/**
	 * @return the capitoloEntrataGestione
	 */
	public CapitoloEntrataGestione getCapitoloEntrataGestione() {
		return capitoloEntrataGestione;
	}

	/**
	 * @param capitoloEntrataGestione the capitoloEntrataGestione to set
	 */
	public void setCapitoloEntrataGestione(
			CapitoloEntrataGestione capitoloEntrataGestione) {
		this.capitoloEntrataGestione = capitoloEntrataGestione;
	}

	/**
	 * @return the importiCapitoloEntrataGestioneM1
	 */
	public ImportiCapitoloEG getImportiCapitoloEntrataGestioneM1() {
		return importiCapitoloEntrataGestioneM1;
	}

	/**
	 * @param importiCapitoloEntrataGestioneM1 the importiCapitoloEntrataGestioneM1 to set
	 */
	public void setImportiCapitoloEntrataGestioneM1(
			ImportiCapitoloEG importiCapitoloEntrataGestioneM1) {
		this.importiCapitoloEntrataGestioneM1 = importiCapitoloEntrataGestioneM1;
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
	public void setImportiCapitoloEntrataGestione0(
			ImportiCapitoloEG importiCapitoloEntrataGestione0) {
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
	public void setImportiCapitoloEntrataGestione1(
			ImportiCapitoloEG importiCapitoloEntrataGestione1) {
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
	public void setImportiCapitoloEntrataGestione2(
			ImportiCapitoloEG importiCapitoloEntrataGestione2) {
		this.importiCapitoloEntrataGestione2 = importiCapitoloEntrataGestione2;
	}

	/**
	 * @return the listaUEBCollegate
	 */
	public List<ElementoCapitolo> getListaUEBCollegate() {
		return listaUEBCollegate;
	}

	/**
	 * @param listaUEBCollegate the listaUEBCollegate to set
	 */
	public void setListaUEBCollegate(List<ElementoCapitolo> listaUEBCollegate) {
		this.listaUEBCollegate = listaUEBCollegate;
	}
	
	/**
	 * @return the annoCapitoloDaConsultare
	 */
	public Integer getAnnoCapitoloDaConsultare() {
		return capitoloEntrataGestione.getAnnoCapitolo();
	}
	
	/**
	 * @param annoCapitoloDaConsultare the annoCapitoloDaConsultare to set
	 */
	public void setAnnoCapitoloDaConsultare(Integer annoCapitoloDaConsultare) {
		if(capitoloEntrataGestione == null) {
			capitoloEntrataGestione = new CapitoloEntrataGestione();
		}
		capitoloEntrataGestione.setAnnoCapitolo(annoCapitoloDaConsultare);
	}
	
	/**
	 * @return the numeroCapitoloDaConsultare
	 */
	public Integer getNumeroCapitoloDaConsultare() {
		return capitoloEntrataGestione.getNumeroCapitolo();
	}
	
	/**
	 * @param numeroCapitoloDaConsultare the numeroCapitoloDaConsultare to set
	 */
	public void setNumeroCapitoloDaConsultare(Integer numeroCapitoloDaConsultare) {
		if(capitoloEntrataGestione == null) {
			capitoloEntrataGestione = new CapitoloEntrataGestione();
		}
		capitoloEntrataGestione.setNumeroCapitolo(numeroCapitoloDaConsultare);
	}
	
	/**
	 * @return the numeroArticoloDaConsultare
	 */
	public Integer getNumeroArticoloDaConsultare() {
		return capitoloEntrataGestione.getNumeroArticolo();
	}
	
	/**
	 * @param numeroArticoloDaConsultare the numeroArticoloDaConsultare to set
	 */
	public void setNumeroArticoloDaConsultare(Integer numeroArticoloDaConsultare) {
		if(capitoloEntrataGestione == null) {
			capitoloEntrataGestione = new CapitoloEntrataGestione();
		}
		capitoloEntrataGestione.setNumeroArticolo(numeroArticoloDaConsultare);
	}
	
	/* Requests */

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioMassivaCapitoloEntrataGestione}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioMassivaCapitoloEntrataGestione creaRequestRicercaDettaglioMassivaCapitoloEntrataGestione() {
		RicercaDettaglioMassivaCapitoloEntrataGestione request = creaRequest(RicercaDettaglioMassivaCapitoloEntrataGestione.class);
		
		request.setEnte(getEnte());
		request.setRicercaSinteticaCapitoloEGest(creaUtilityRicercaSinteticaCapitoloEGest());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link ControllaClassificatoriModificabiliCapitolo}.
	 * 
	 * @return la request creata
	 */
	public ControllaClassificatoriModificabiliCapitolo creaRequestControllaClassificatoriModificabiliCapitolo() {
		ControllaClassificatoriModificabiliCapitolo request = creaRequest(ControllaClassificatoriModificabiliCapitolo.class);
		request.setBilancio(getBilancio());
		request.setEnte(getEnte());
		
		// Imposto numero capitolo e numero articolo. Non il numero della UEB
		request.setNumeroCapitolo(getNumeroCapitoloDaConsultare());
		request.setNumeroArticolo(getNumeroArticoloDaConsultare());
		
		request.setTipoCapitolo(TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE);
		
		return request;
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Metodo di utilit&agrave; per la creazione dei parametri di ricerca.
	 * 
	 * @return i parametri
	 */
	private RicercaSinteticaCapitoloEGest creaUtilityRicercaSinteticaCapitoloEGest() {
		RicercaSinteticaCapitoloEGest utility = new RicercaSinteticaCapitoloEGest();
		
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setAnnoCapitolo(getAnnoCapitoloDaConsultare());
		utility.setNumeroCapitolo(getNumeroCapitoloDaConsultare());
		utility.setNumeroArticolo(getNumeroArticoloDaConsultare());
		
		return utility;
	}
	
	/**
	 * Imposta la consultabilit&agrave; dei classificatori nel model.
	 * 
	 * @param response la response da cui ricavare le consultabilit&agrave;
	 */
	public void valutaConsultabilitaClassificatori(ControllaClassificatoriModificabiliCapitoloResponse response) {
		// Se sono modificabili nell'aggiornamento massivo, allora sono consultabili nella consultazione massiva
		
		// Classificatori del capitolo di Entrata
		setTitoloEntrataEditabile(response.isModificabileMassivo(TipologiaClassificatore.TITOLO_ENTRATA));
		setTipologiaTitoloEditabile(response.isModificabileMassivo(TipologiaClassificatore.TIPOLOGIA));
		setCategoriaTipologiaTitoloEditabile(response.isModificabileMassivo(TipologiaClassificatore.CATEGORIA));
		
		// Elemento del piano dei conti
		setElementoPianoDeiContiEditabile(response.isModificabileMassivo(TipologiaClassificatore.PDC_I) ||
				response.isModificabileMassivo(TipologiaClassificatore.PDC_II) ||
				response.isModificabileMassivo(TipologiaClassificatore.PDC_III) ||
				response.isModificabileMassivo(TipologiaClassificatore.PDC_IV) ||
				response.isModificabileMassivo(TipologiaClassificatore.PDC_V));
		// Struttura amministrativa contabile
		setStrutturaAmministrativoContabileEditabile(response.isModificabileMassivo(TipologiaClassificatore.CDC) ||
				response.isModificabileMassivo(TipologiaClassificatore.CDR));
		
		setTipoFinanziamentoEditabile(response.isModificabileMassivo(TipologiaClassificatore.TIPO_FINANZIAMENTO));
		setTipoFondoEditabile(response.isModificabileMassivo(TipologiaClassificatore.TIPO_FONDO));
		// Classificatori generici
		setClassificatoreGenerico1Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_36));
		setClassificatoreGenerico2Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_37));
		setClassificatoreGenerico3Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_38));
		setClassificatoreGenerico4Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_39));
		setClassificatoreGenerico5Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_40));
		setClassificatoreGenerico6Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_41));
		setClassificatoreGenerico7Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_42));
		setClassificatoreGenerico8Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_43));
		setClassificatoreGenerico9Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_44));
		setClassificatoreGenerico10Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_45));
		setClassificatoreGenerico11Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_46));
		setClassificatoreGenerico12Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_47));
		setClassificatoreGenerico13Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_48));
		setClassificatoreGenerico14Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_49));
		setClassificatoreGenerico15Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_50));
		
		setSiopeEntrataEditabile(response.isModificabileMassivo(TipologiaClassificatore.SIOPE_ENTRATA) ||
				response.isModificabileMassivo(TipologiaClassificatore.SIOPE_ENTRATA_I) ||
				response.isModificabileMassivo(TipologiaClassificatore.SIOPE_ENTRATA_II) ||
				response.isModificabileMassivo(TipologiaClassificatore.SIOPE_ENTRATA_III));
		setRicorrenteEntrataEditabile(response.isModificabileMassivo(TipologiaClassificatore.RICORRENTE_ENTRATA));
		setPerimetroSanitarioEntrataEditabile(response.isModificabileMassivo(TipologiaClassificatore.PERIMETRO_SANITARIO_ENTRATA));
		setTransazioneUnioneEuropeaEntrataEditabile(response.isModificabileMassivo(TipologiaClassificatore.TRANSAZIONE_UE_ENTRATA));
		
		setFlagImpegnabileEditabile(true);
		setCategoriaCapitoloEditabile(true);
		
	}
	
	/**
	 * Metodo di utilit&agrave; per l'impostazione nel model dei dati ottenuti dalla response del servizio e dalla sessione.
	 * 
	 * @param response la response da cui popolare il model
	 * @param sessionHandler l'handler per la sessione
	 * 
	 */
	public void impostaDatiDaResponseESessione(RicercaDettaglioMassivaCapitoloEntrataGestioneResponse response, SessionHandler sessionHandler) {
		// Dovrebbe essere l'unico capitolo presente
		CapitoloMassivaEntrataGestione capitoloInResponse = response.getCapitoloMassivaEntrataGestione();
		
		//Popolamento dei campi
		capitoloEntrataGestione = capitoloInResponse;
		
		/* Ottenuti dalla sessione */
		impostaLabelDaSessione(sessionHandler);
		
		// Classificatori
		setTipoFinanziamento(capitoloInResponse.getTipoFinanziamento());
		setTipoFondo(capitoloInResponse.getTipoFondo());
		impostaClassificatoriGenericiDaLista(capitoloInResponse.getListaClassificatori());
		
		impostaImportiCapitoloEntrataGestione(capitoloInResponse.getListaImportiCapitoloEG());
		
		setTitoloEntrata(capitoloInResponse.getTitoloEntrata());
		setTipologiaTitolo(capitoloInResponse.getTipologiaTitolo());
		setCategoriaTipologiaTitolo(capitoloInResponse.getCategoriaTipologiaTitolo());
		setStrutturaAmministrativoContabile(capitoloInResponse.getStrutturaAmministrativoContabile());
		setElementoPianoDeiConti(capitoloInResponse.getElementoPianoDeiConti());
		
		setSiopeEntrata(capitoloInResponse.getSiopeEntrata());
		setRicorrenteEntrata(capitoloInResponse.getRicorrenteEntrata());
		setPerimetroSanitarioEntrata(capitoloInResponse.getPerimetroSanitarioEntrata());
		setTransazioneUnioneEuropeaEntrata(capitoloInResponse.getTransazioneUnioneEuropeaEntrata());
		
		listaUEBCollegate = ElementoCapitoloFactory.getInstances(response.getCapitoloMassivaEntrataGestione().getElencoCapitoli(), false, isGestioneUEB());
	}
	
	/**
	 * Imposta gli importi del Capitolo di Entrata Gestione a partire dalla Response del servizio di 
	 * {@link RicercaDettaglioCapitoloEntrataGestione}.
	 * 
	 * @param listaImporti la lista degli importi
	 */
	private void impostaImportiCapitoloEntrataGestione(List<ImportiCapitoloEG> listaImporti) {
		Integer anno = getAnnoEsercizioInt();
		try {
			int annoInt = anno.intValue();
			importiCapitoloEntrataGestione0 = ComparatorUtils.searchByAnno(listaImporti, anno);
			importiCapitoloEntrataGestione1 = ComparatorUtils.searchByAnno(listaImporti, Integer.valueOf(annoInt + 1));
			importiCapitoloEntrataGestione2 = ComparatorUtils.searchByAnno(listaImporti, Integer.valueOf(annoInt + 2));
			importiCapitoloEntrataGestioneM1 = ComparatorUtils.searchByAnno(listaImporti, Integer.valueOf(annoInt - 1));
		} catch(Exception e) {
			// Ignoro l'errore: ipmorti non impostabili
		}
	}
	
}
