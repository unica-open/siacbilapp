/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capentprev;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloEntrataPrevisioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitoloFactory;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CapitoloMassivaEntrataPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEP;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEPrev;
import it.csi.siac.siaccommonapp.handler.session.SessionHandler;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;

/**
 * Classe di model per la consultazione massiva del Capitolo di Entrata Previsione. Contiene una mappatura dei form per il
 * Capitolo di Entrata Previsione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 23/09/2013
 *
 */
public class ConsultaMassivaCapitoloEntrataPrevisioneModel extends CapitoloEntrataPrevisioneModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7468063563092832776L;
	
	/* Maschera 1 */
	private CapitoloEntrataPrevisione capitoloEntrataPrevisione;
	
	private ImportiCapitoloEP importiCapitoloEntrataPrevisioneM1;
	private ImportiCapitoloEP importiCapitoloEntrataPrevisione0;
	private ImportiCapitoloEP importiCapitoloEntrataPrevisione1;
	private ImportiCapitoloEP importiCapitoloEntrataPrevisione2;
	
	/* Maschera 2: UEB collegate */
	private List<ElementoCapitolo> listaUEBCollegate = new ArrayList<ElementoCapitolo>();
	
	/** Costruttore vuoto di default */
	public ConsultaMassivaCapitoloEntrataPrevisioneModel() {
		super();
		setTitolo("Consulta Capitolo Entrata Previsione (Massivo)");
	}
	
	/* Getter e Setter */

	/**
	 * @return the capitoloEntrataPrevisione
	 */
	public CapitoloEntrataPrevisione getCapitoloEntrataPrevisione() {
		return capitoloEntrataPrevisione;
	}

	/**
	 * @param capitoloEntrataPrevisione the capitoloEntrataPrevisione to set
	 */
	public void setCapitoloEntrataPrevisione(
			CapitoloEntrataPrevisione capitoloEntrataPrevisione) {
		this.capitoloEntrataPrevisione = capitoloEntrataPrevisione;
	}

	/**
	 * @return the importiCapitoloEntrataPrevisioneM1
	 */
	public ImportiCapitoloEP getImportiCapitoloEntrataPrevisioneM1() {
		return importiCapitoloEntrataPrevisioneM1;
	}

	/**
	 * @param importiCapitoloEntrataPrevisioneM1 the importiCapitoloEntrataPrevisioneM1 to set
	 */
	public void setImportiCapitoloEntrataPrevisioneM1(
			ImportiCapitoloEP importiCapitoloEntrataPrevisioneM1) {
		this.importiCapitoloEntrataPrevisioneM1 = importiCapitoloEntrataPrevisioneM1;
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
	public void setImportiCapitoloEntrataPrevisione0(
			ImportiCapitoloEP importiCapitoloEntrataPrevisione0) {
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
	public void setImportiCapitoloEntrataPrevisione1(
			ImportiCapitoloEP importiCapitoloEntrataPrevisione1) {
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
	public void setImportiCapitoloEntrataPrevisione2(
			ImportiCapitoloEP importiCapitoloEntrataPrevisione2) {
		this.importiCapitoloEntrataPrevisione2 = importiCapitoloEntrataPrevisione2;
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
		return capitoloEntrataPrevisione.getAnnoCapitolo();
	}
	
	/**
	 * @param annoCapitoloDaConsultare the annoCapitoloDaConsultare to set
	 */
	public void setAnnoCapitoloDaConsultare(Integer annoCapitoloDaConsultare) {
		if(capitoloEntrataPrevisione == null) {
			capitoloEntrataPrevisione = new CapitoloEntrataPrevisione();
		}
		capitoloEntrataPrevisione.setAnnoCapitolo(annoCapitoloDaConsultare);
	}
	
	/**
	 * @return the numeroCapitoloDaConsultare
	 */
	public Integer getNumeroCapitoloDaConsultare() {
		return capitoloEntrataPrevisione.getNumeroCapitolo();
	}
	
	/**
	 * @param numeroCapitoloDaConsultare the numeroCapitoloDaConsultare to set
	 */
	public void setNumeroCapitoloDaConsultare(Integer numeroCapitoloDaConsultare) {
		if(capitoloEntrataPrevisione == null) {
			capitoloEntrataPrevisione = new CapitoloEntrataPrevisione();
		}
		capitoloEntrataPrevisione.setNumeroCapitolo(numeroCapitoloDaConsultare);
	}
	
	/**
	 * @return the numeroArticoloDaConsultare
	 */
	public Integer getNumeroArticoloDaConsultare() {
		return capitoloEntrataPrevisione.getNumeroArticolo();
	}
	
	/**
	 * @param numeroArticoloDaConsultare the numeroArticoloDaConsultare to set
	 */
	public void setNumeroArticoloDaConsultare(Integer numeroArticoloDaConsultare) {
		if(capitoloEntrataPrevisione == null) {
			capitoloEntrataPrevisione = new CapitoloEntrataPrevisione();
		}
		capitoloEntrataPrevisione.setNumeroArticolo(numeroArticoloDaConsultare);
	}
	
	/* Requests */

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioMassivaCapitoloEntrataPrevisione}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioMassivaCapitoloEntrataPrevisione creaRequestRicercaDettaglioMassivaCapitoloEntrataPrevisione() {
		RicercaDettaglioMassivaCapitoloEntrataPrevisione request = creaRequest(RicercaDettaglioMassivaCapitoloEntrataPrevisione.class);
		
		request.setEnte(getEnte());
		request.setRicercaSinteticaCapitoloEPrev(creaUtilityRicercaSinteticaCapitoloEPrev());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link ControllaClassificatoriModificabiliCapitolo}.
	 * 
	 * @return la request creata
	 */
	public ControllaClassificatoriModificabiliCapitolo creaRequestControllaClassificatoriModificabiliCapitolo() {
		ControllaClassificatoriModificabiliCapitolo request =creaRequest(ControllaClassificatoriModificabiliCapitolo.class);
		request.setBilancio(getBilancio());
		request.setEnte(getEnte());
		
		// Imposto numero capitolo e numero articolo. Non il numero della UEB
		request.setNumeroCapitolo(getNumeroCapitoloDaConsultare());
		request.setNumeroArticolo(getNumeroArticoloDaConsultare());
		
		request.setTipoCapitolo(TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE);
		
		return request;
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Metodo di utilit&agrave; per la creazione dei parametri di ricerca.
	 * 
	 * @return i parametri
	 */
	private RicercaSinteticaCapitoloEPrev creaUtilityRicercaSinteticaCapitoloEPrev() {
		RicercaSinteticaCapitoloEPrev utility = new RicercaSinteticaCapitoloEPrev();
		
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
	public void impostaDatiDaResponseESessione(RicercaDettaglioMassivaCapitoloEntrataPrevisioneResponse response, SessionHandler sessionHandler) {
		// Dovrebbe essere l'unico capitolo presente
		CapitoloMassivaEntrataPrevisione capitoloInResponse = response.getCapitoloMassivaEntrataPrevisione();
		
		//Popolamento dei campi
		capitoloEntrataPrevisione = capitoloInResponse;
		
		/* Ottenuti dalla sessione */
		impostaLabelDaSessione(sessionHandler);
		
		// Classificatori
		setTipoFinanziamento(capitoloInResponse.getTipoFinanziamento());
		setTipoFondo(capitoloInResponse.getTipoFondo());
		impostaClassificatoriGenericiDaLista(capitoloInResponse.getListaClassificatori());
		
		impostaImportiCapitoloEntrataPrevisione(capitoloInResponse.getListaImportiCapitoloEP());
		
		setTitoloEntrata(capitoloInResponse.getTitoloEntrata());
		setTipologiaTitolo(capitoloInResponse.getTipologiaTitolo());
		setCategoriaTipologiaTitolo(capitoloInResponse.getCategoriaTipologiaTitolo());
		setStrutturaAmministrativoContabile(capitoloInResponse.getStrutturaAmministrativoContabile());
		setElementoPianoDeiConti(capitoloInResponse.getElementoPianoDeiConti());
		
		setSiopeEntrata(capitoloInResponse.getSiopeEntrata());
		setRicorrenteEntrata(capitoloInResponse.getRicorrenteEntrata());
		setPerimetroSanitarioEntrata(capitoloInResponse.getPerimetroSanitarioEntrata());
		setTransazioneUnioneEuropeaEntrata(capitoloInResponse.getTransazioneUnioneEuropeaEntrata());
		
		listaUEBCollegate = ElementoCapitoloFactory.getInstances(response.getCapitoloMassivaEntrataPrevisione().getElencoCapitoli(), false, isGestioneUEB());
	}
	
	/**
	 * Imposta gli importi del Capitolo di Entrata Previsione a partire dalla Response del servizio di 
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
			importiCapitoloEntrataPrevisioneM1 = ComparatorUtils.searchByAnno(listaImporti, Integer.valueOf(annoInt - 1));
		} catch(Exception e) {
			// Ignoro l'errore: ipmorti non impostabili
		}
	}
	
}
