/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capuscgest;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloUscitaModel;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitoloFactory;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloMassivaUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUGest;
import it.csi.siac.siaccommonapp.handler.session.SessionHandler;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;

/**
 * Classe di model per la consultazione massiva del Capitolo di Uscita Gestione. Contiene una mappatura dei form per il
 * Capitolo di Uscita Gestione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 23/09/2013
 *
 */
public class ConsultaMassivaCapitoloUscitaGestioneModel extends CapitoloUscitaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7336992775320509905L;
	
	/* Maschera 1 */
	private CapitoloUscitaGestione capitoloUscitaGestione;
	
	private ImportiCapitoloUG importiCapitoloUscitaGestioneM1;
	private ImportiCapitoloUG importiCapitoloUscitaGestione0;
	private ImportiCapitoloUG importiCapitoloUscitaGestione1;
	private ImportiCapitoloUG importiCapitoloUscitaGestione2;
	
	/* Maschera 2: UEB collegate */
	private List<ElementoCapitolo> listaUEBCollegate = new ArrayList<ElementoCapitolo>();
	
	/** Costruttore vuoto di default */
	public ConsultaMassivaCapitoloUscitaGestioneModel() {
		super();
		setTitolo("Consulta Capitolo Spesa Gestione (Massivo)");
	}
	
	/* Getter e Setter */

	/**
	 * @return the capitoloUscitaGestione
	 */
	public CapitoloUscitaGestione getCapitoloUscitaGestione() {
		return capitoloUscitaGestione;
	}

	/**
	 * @param capitoloUscitaGestione the capitoloUscitaGestione to set
	 */
	public void setCapitoloUscitaGestione(
			CapitoloUscitaGestione capitoloUscitaGestione) {
		this.capitoloUscitaGestione = capitoloUscitaGestione;
	}

	/**
	 * @return the importiCapitoloUscitaGestioneM1
	 */
	public ImportiCapitoloUG getImportiCapitoloUscitaGestioneM1() {
		return importiCapitoloUscitaGestioneM1;
	}

	/**
	 * @param importiCapitoloUscitaGestioneM1 the importiCapitoloUscitaGestioneM1 to set
	 */
	public void setImportiCapitoloUscitaGestioneM1(
			ImportiCapitoloUG importiCapitoloUscitaGestioneM1) {
		this.importiCapitoloUscitaGestioneM1 = importiCapitoloUscitaGestioneM1;
	}

	/**
	 * @return the importiCapitoloUscitaGestione0
	 */
	public ImportiCapitoloUG getImportiCapitoloUscitaGestione0() {
		return importiCapitoloUscitaGestione0;
	}

	/**
	 * @param importiCapitoloUscitaGestione0 the importiCapitoloUscitaGestione0 to set
	 */
	public void setImportiCapitoloUscitaGestione0(
			ImportiCapitoloUG importiCapitoloUscitaGestione0) {
		this.importiCapitoloUscitaGestione0 = importiCapitoloUscitaGestione0;
	}

	/**
	 * @return the importiCapitoloUscitaGestione1
	 */
	public ImportiCapitoloUG getImportiCapitoloUscitaGestione1() {
		return importiCapitoloUscitaGestione1;
	}

	/**
	 * @param importiCapitoloUscitaGestione1 the importiCapitoloUscitaGestione1 to set
	 */
	public void setImportiCapitoloUscitaGestione1(
			ImportiCapitoloUG importiCapitoloUscitaGestione1) {
		this.importiCapitoloUscitaGestione1 = importiCapitoloUscitaGestione1;
	}

	/**
	 * @return the importiCapitoloUscitaGestione2
	 */
	public ImportiCapitoloUG getImportiCapitoloUscitaGestione2() {
		return importiCapitoloUscitaGestione2;
	}

	/**
	 * @param importiCapitoloUscitaGestione2 the importiCapitoloUscitaGestione2 to set
	 */
	public void setImportiCapitoloUscitaGestione2(
			ImportiCapitoloUG importiCapitoloUscitaGestione2) {
		this.importiCapitoloUscitaGestione2 = importiCapitoloUscitaGestione2;
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
		return capitoloUscitaGestione.getAnnoCapitolo();
	}
	
	/**
	 * @param annoCapitoloDaConsultare the annoCapitoloDaConsultare to set
	 */
	public void setAnnoCapitoloDaConsultare(Integer annoCapitoloDaConsultare) {
		if(capitoloUscitaGestione == null) {
			capitoloUscitaGestione = new CapitoloUscitaGestione();
		}
		capitoloUscitaGestione.setAnnoCapitolo(annoCapitoloDaConsultare);
	}
	
	/**
	 * @return the numeroCapitoloDaConsultare
	 */
	public Integer getNumeroCapitoloDaConsultare() {
		return capitoloUscitaGestione.getNumeroCapitolo();
	}
	
	/**
	 * @param numeroCapitoloDaConsultare the numeroCapitoloDaConsultare to set
	 */
	public void setNumeroCapitoloDaConsultare(Integer numeroCapitoloDaConsultare) {
		if(capitoloUscitaGestione == null) {
			capitoloUscitaGestione = new CapitoloUscitaGestione();
		}
		capitoloUscitaGestione.setNumeroCapitolo(numeroCapitoloDaConsultare);
	}
	
	/**
	 * @return the numeroArticoloDaConsultare
	 */
	public Integer getNumeroArticoloDaConsultare() {
		return capitoloUscitaGestione.getNumeroArticolo();
	}
	
	/**
	 * @param numeroArticoloDaConsultare the numeroArticoloDaConsultare to set
	 */
	public void setNumeroArticoloDaConsultare(Integer numeroArticoloDaConsultare) {
		if(capitoloUscitaGestione == null) {
			capitoloUscitaGestione = new CapitoloUscitaGestione();
		}
		capitoloUscitaGestione.setNumeroArticolo(numeroArticoloDaConsultare);
	}
	
	/* Requests */

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioMassivaCapitoloUscitaGestione}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioMassivaCapitoloUscitaGestione creaRequestRicercaDettaglioMassivaCapitoloUscitaGestione() {
		RicercaDettaglioMassivaCapitoloUscitaGestione request = creaRequest(RicercaDettaglioMassivaCapitoloUscitaGestione.class);
				
		request.setEnte(getEnte());
		request.setRicercaSinteticaCapitoloUGest(creaUtilityRicercaSinteticaCapitoloUGest());
		
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
		
		request.setTipoCapitolo(TipoCapitolo.CAPITOLO_USCITA_GESTIONE);
		
		return request;
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Metodo di utilit&agrave; per la creazione dei parametri di ricerca.
	 * 
	 * @return i parametri
	 */
	private RicercaSinteticaCapitoloUGest creaUtilityRicercaSinteticaCapitoloUGest() {
		RicercaSinteticaCapitoloUGest utility = new RicercaSinteticaCapitoloUGest();
		
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
	public void impostaDatiDaResponseESessione(RicercaDettaglioMassivaCapitoloUscitaGestioneResponse response, SessionHandler sessionHandler) {
		// Dovrebbe essere l'unico capitolo presente
		CapitoloMassivaUscitaGestione capitoloInResponse = response.getCapitoloMassivaUscitaGestione();
		
		//Popolamento dei campi
		capitoloUscitaGestione = capitoloInResponse;
		
		/* Ottenuti dalla sessione */
		impostaLabelDaSessione(sessionHandler);
		
		// Classificatori
		setTipoFinanziamento(capitoloInResponse.getTipoFinanziamento());
		setTipoFondo(capitoloInResponse.getTipoFondo());
		impostaClassificatoriGenericiDaLista(capitoloInResponse.getListaClassificatori());
		
		impostaImportiCapitoloUscitaGestione(capitoloInResponse.getListaImportiCapitoloUG());
		
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
		
		listaUEBCollegate = ElementoCapitoloFactory.getInstances(response.getCapitoloMassivaUscitaGestione().getElencoCapitoli(), false, isGestioneUEB());
	}
	
	/**
	 * Imposta gli importi del Capitolo di Uscita Gestione a partire dalla Response del servizio di 
	 * {@link RicercaDettaglioCapitoloUscitaGestione}.
	 * 
	 * @param listaImporti la lista degli importi
	 */
	private void impostaImportiCapitoloUscitaGestione(List<ImportiCapitoloUG> listaImporti) {
		Integer anno = getAnnoEsercizioInt();
		try {
			int annoInt = anno.intValue();
			importiCapitoloUscitaGestione0 = ComparatorUtils.searchByAnno(listaImporti, anno);
			importiCapitoloUscitaGestione1 = ComparatorUtils.searchByAnno(listaImporti, Integer.valueOf(annoInt + 1));
			importiCapitoloUscitaGestione2 = ComparatorUtils.searchByAnno(listaImporti, Integer.valueOf(annoInt + 2));
			importiCapitoloUscitaGestioneM1 = ComparatorUtils.searchByAnno(listaImporti, Integer.valueOf(annoInt - 1));
		} catch(Exception e) {
			// Ignoro l'errore: ipmorti non impostabili
		}
	}
	
}
