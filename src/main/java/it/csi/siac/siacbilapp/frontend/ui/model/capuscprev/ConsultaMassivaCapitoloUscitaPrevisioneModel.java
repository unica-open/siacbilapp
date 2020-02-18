/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capuscprev;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloUscitaPrevisioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitoloFactory;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloMassivaUscitaPrevisione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUPrev;
import it.csi.siac.siaccommonapp.handler.session.SessionHandler;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;

/**
 * Classe di model per la consultazione massiva del Capitolo di Uscita Previsione. Contiene una mappatura dei form per il
 * Capitolo di Uscita Previsione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 23/09/2013
 *
 */
public class ConsultaMassivaCapitoloUscitaPrevisioneModel extends CapitoloUscitaPrevisioneModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7118246537482469065L;
	
	/* Maschera 1 */
	private CapitoloUscitaPrevisione capitoloUscitaPrevisione;
	
	private ImportiCapitoloUP importiCapitoloUscitaPrevisioneM1;
	private ImportiCapitoloUP importiCapitoloUscitaPrevisione0;
	private ImportiCapitoloUP importiCapitoloUscitaPrevisione1;
	private ImportiCapitoloUP importiCapitoloUscitaPrevisione2;
	
	/* Maschera 2: UEB collegate */
	private List<ElementoCapitolo> listaUEBCollegate = new ArrayList<ElementoCapitolo>();
	
	/** Costruttore vuoto di default */
	public ConsultaMassivaCapitoloUscitaPrevisioneModel() {
		super();
		setTitolo("Consulta Capitolo Spesa Previsione (Massivo)");
	}
	
	/* Getter e Setter */

	/**
	 * @return the capitoloUscitaPrevisione
	 */
	public CapitoloUscitaPrevisione getCapitoloUscitaPrevisione() {
		return capitoloUscitaPrevisione;
	}

	/**
	 * @param capitoloUscitaPrevisione the capitoloUscitaPrevisione to set
	 */
	public void setCapitoloUscitaPrevisione(
			CapitoloUscitaPrevisione capitoloUscitaPrevisione) {
		this.capitoloUscitaPrevisione = capitoloUscitaPrevisione;
	}

	/**
	 * @return the importiCapitoloUscitaPrevisioneM1
	 */
	public ImportiCapitoloUP getImportiCapitoloUscitaPrevisioneM1() {
		return importiCapitoloUscitaPrevisioneM1;
	}

	/**
	 * @param importiCapitoloUscitaPrevisioneM1 the importiCapitoloUscitaPrevisioneM1 to set
	 */
	public void setImportiCapitoloUscitaPrevisioneM1(
			ImportiCapitoloUP importiCapitoloUscitaPrevisioneM1) {
		this.importiCapitoloUscitaPrevisioneM1 = importiCapitoloUscitaPrevisioneM1;
	}

	/**
	 * @return the importiCapitoloUscitaPrevisione0
	 */
	public ImportiCapitoloUP getImportiCapitoloUscitaPrevisione0() {
		return importiCapitoloUscitaPrevisione0;
	}

	/**
	 * @param importiCapitoloUscitaPrevisione0 the importiCapitoloUscitaPrevisione0 to set
	 */
	public void setImportiCapitoloUscitaPrevisione0(
			ImportiCapitoloUP importiCapitoloUscitaPrevisione0) {
		this.importiCapitoloUscitaPrevisione0 = importiCapitoloUscitaPrevisione0;
	}

	/**
	 * @return the importiCapitoloUscitaPrevisione1
	 */
	public ImportiCapitoloUP getImportiCapitoloUscitaPrevisione1() {
		return importiCapitoloUscitaPrevisione1;
	}

	/**
	 * @param importiCapitoloUscitaPrevisione1 the importiCapitoloUscitaPrevisione1 to set
	 */
	public void setImportiCapitoloUscitaPrevisione1(
			ImportiCapitoloUP importiCapitoloUscitaPrevisione1) {
		this.importiCapitoloUscitaPrevisione1 = importiCapitoloUscitaPrevisione1;
	}

	/**
	 * @return the importiCapitoloUscitaPrevisione2
	 */
	public ImportiCapitoloUP getImportiCapitoloUscitaPrevisione2() {
		return importiCapitoloUscitaPrevisione2;
	}

	/**
	 * @param importiCapitoloUscitaPrevisione2 the importiCapitoloUscitaPrevisione2 to set
	 */
	public void setImportiCapitoloUscitaPrevisione2(
			ImportiCapitoloUP importiCapitoloUscitaPrevisione2) {
		this.importiCapitoloUscitaPrevisione2 = importiCapitoloUscitaPrevisione2;
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
		return capitoloUscitaPrevisione.getAnnoCapitolo();
	}
	
	/**
	 * @param annoCapitoloDaConsultare the annoCapitoloDaConsultare to set
	 */
	public void setAnnoCapitoloDaConsultare(Integer annoCapitoloDaConsultare) {
		if(capitoloUscitaPrevisione == null) {
			capitoloUscitaPrevisione = new CapitoloUscitaPrevisione();
		}
		capitoloUscitaPrevisione.setAnnoCapitolo(annoCapitoloDaConsultare);
	}
	
	/**
	 * @return the numeroCapitoloDaConsultare
	 */
	public Integer getNumeroCapitoloDaConsultare() {
		return capitoloUscitaPrevisione.getNumeroCapitolo();
	}
	
	/**
	 * @param numeroCapitoloDaConsultare the numeroCapitoloDaConsultare to set
	 */
	public void setNumeroCapitoloDaConsultare(Integer numeroCapitoloDaConsultare) {
		if(capitoloUscitaPrevisione == null) {
			capitoloUscitaPrevisione = new CapitoloUscitaPrevisione();
		}
		capitoloUscitaPrevisione.setNumeroCapitolo(numeroCapitoloDaConsultare);
	}
	
	/**
	 * @return the numeroArticoloDaConsultare
	 */
	public Integer getNumeroArticoloDaConsultare() {
		return capitoloUscitaPrevisione.getNumeroArticolo();
	}
	
	/**
	 * @param numeroArticoloDaConsultare the numeroArticoloDaConsultare to set
	 */
	public void setNumeroArticoloDaConsultare(Integer numeroArticoloDaConsultare) {
		if(capitoloUscitaPrevisione == null) {
			capitoloUscitaPrevisione = new CapitoloUscitaPrevisione();
		}
		capitoloUscitaPrevisione.setNumeroArticolo(numeroArticoloDaConsultare);
	}
		
	/* Requests */

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioMassivaCapitoloUscitaPrevisione}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioMassivaCapitoloUscitaPrevisione creaRequestRicercaDettaglioMassivaCapitoloUscitaPrevisione() {
		RicercaDettaglioMassivaCapitoloUscitaPrevisione request = creaRequest(RicercaDettaglioMassivaCapitoloUscitaPrevisione.class);
		
		request.setEnte(getEnte());
		request.setRicercaSinteticaCapitoloUPrev(creaUtilityRicercaSinteticaCapitoloUPrev());
		
		return request;
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Metodo di utilit&agrave; per la creazione dei parametri di ricerca.
	 * 
	 * @return i parametri
	 */
	private RicercaSinteticaCapitoloUPrev creaUtilityRicercaSinteticaCapitoloUPrev() {
		RicercaSinteticaCapitoloUPrev utility = new RicercaSinteticaCapitoloUPrev();
		
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
		
		// Classificatori del capitolo di uscita
		setMissioneEditabile(response.isModificabileMassivo(TipologiaClassificatore.MISSIONE));
		setProgrammaEditabile(response.isModificabileMassivo(TipologiaClassificatore.PROGRAMMA));
		setClassificazioneCofogEditabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSE_COFOG));
		setTitoloSpesaEditabile(response.isModificabileMassivo(TipologiaClassificatore.TITOLO_SPESA));
		setMacroaggregatoEditabile(response.isModificabileMassivo(TipologiaClassificatore.MACROAGGREGATO));
		
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
		setClassificatoreGenerico1Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_1));
		setClassificatoreGenerico2Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_2));
		setClassificatoreGenerico3Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_3));
		setClassificatoreGenerico4Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_4));
		setClassificatoreGenerico5Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_5));
		setClassificatoreGenerico6Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_6));
		setClassificatoreGenerico7Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_7));
		setClassificatoreGenerico8Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_8));
		setClassificatoreGenerico9Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_9));
		setClassificatoreGenerico10Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_10));
		setClassificatoreGenerico11Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_31));
		setClassificatoreGenerico12Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_32));
		setClassificatoreGenerico13Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_33));
		setClassificatoreGenerico14Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_34));
		setClassificatoreGenerico15Editabile(response.isModificabileMassivo(TipologiaClassificatore.CLASSIFICATORE_35));
		
		setSiopeSpesaEditabile(response.isModificabileMassivo(TipologiaClassificatore.SIOPE_SPESA) ||
				response.isModificabileMassivo(TipologiaClassificatore.SIOPE_SPESA_I) ||
				response.isModificabileMassivo(TipologiaClassificatore.SIOPE_SPESA_II) ||
				response.isModificabileMassivo(TipologiaClassificatore.SIOPE_SPESA_III));
		setRicorrenteSpesaEditabile(response.isModificabileMassivo(TipologiaClassificatore.RICORRENTE_SPESA));
		setPerimetroSanitarioSpesaEditabile(response.isModificabileMassivo(TipologiaClassificatore.PERIMETRO_SANITARIO_SPESA));
		setTransazioneUnioneEuropeaSpesaEditabile(response.isModificabileMassivo(TipologiaClassificatore.TRANSAZIONE_UE_SPESA));
		setPoliticheRegionaliUnitarieEditabile(response.isModificabileMassivo(TipologiaClassificatore.POLITICHE_REGIONALI_UNITARIE));
		
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
	public void impostaDatiDaResponseESessione(RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse response, SessionHandler sessionHandler) {
		// Dovrebbe essere l'unico capitolo presente
		CapitoloMassivaUscitaPrevisione capitoloInResponse = response.getCapitoloMassivaUscitaPrevisione();
		
		//Popolamento dei campi
		capitoloUscitaPrevisione = capitoloInResponse;
		
		/* Ottenuti dalla sessione */
		impostaLabelDaSessione(sessionHandler);
		
		// Classificatori
		setTipoFinanziamento(capitoloInResponse.getTipoFinanziamento());
		setTipoFondo(capitoloInResponse.getTipoFondo());
		impostaClassificatoriGenericiDaLista(capitoloInResponse.getListaClassificatori());
		
		impostaImportiCapitoloUscitaPrevisione(capitoloInResponse.getListaImportiCapitoloUP());
		
		setMissione(capitoloInResponse.getMissione());
		setProgramma(capitoloInResponse.getProgramma());
		setClassificazioneCofog(capitoloInResponse.getClassificazioneCofog());
		setTitoloSpesa(capitoloInResponse.getTitoloSpesa());
		setMacroaggregato(capitoloInResponse.getMacroaggregato());
		setStrutturaAmministrativoContabile(capitoloInResponse.getStrutturaAmministrativoContabile());
		setElementoPianoDeiConti(capitoloInResponse.getElementoPianoDeiConti());
		
		setSiopeSpesa(capitoloInResponse.getSiopeSpesa());
		setRicorrenteSpesa(capitoloInResponse.getRicorrenteSpesa());
		setPerimetroSanitarioSpesa(capitoloInResponse.getPerimetroSanitarioSpesa());
		setTransazioneUnioneEuropeaSpesa(capitoloInResponse.getTransazioneUnioneEuropeaSpesa());
		setPoliticheRegionaliUnitarie(capitoloInResponse.getPoliticheRegionaliUnitarie());
		
		listaUEBCollegate = ElementoCapitoloFactory.getInstances(response.getCapitoloMassivaUscitaPrevisione().getElencoCapitoli(), false, isGestioneUEB());
	}
		
	/**
	 * Imposta gli importi del Capitolo di Uscita Previsione a partire dalla Response del servizio di 
	 * {@link RicercaDettaglioCapitoloUscitaPrevisione}.
	 * 
	 * @param listaImporti la lista degli importi
	 */
	private void impostaImportiCapitoloUscitaPrevisione(List<ImportiCapitoloUP> listaImporti) {
		Integer anno = getAnnoEsercizioInt();
		try {
			int annoInt = anno.intValue();
			importiCapitoloUscitaPrevisione0 = ComparatorUtils.searchByAnno(listaImporti, anno);
			importiCapitoloUscitaPrevisione1 = ComparatorUtils.searchByAnno(listaImporti, Integer.valueOf(annoInt + 1));
			importiCapitoloUscitaPrevisione2 = ComparatorUtils.searchByAnno(listaImporti, Integer.valueOf(annoInt + 2));
			importiCapitoloUscitaPrevisioneM1 = ComparatorUtils.searchByAnno(listaImporti, Integer.valueOf(annoInt - 1));
		} catch(Exception e) {
			// Ignoro l'errore: ipmorti non impostabili
		}
	}
	
}
