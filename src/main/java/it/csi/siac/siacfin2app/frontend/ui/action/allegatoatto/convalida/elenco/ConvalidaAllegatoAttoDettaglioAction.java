/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto.convalida.elenco;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoElencoDocumentiAllegato;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoElencoDocumentiAllegatoEntrata;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoElencoDocumentiAllegatoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerElenchi;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioElencoResponse;
import it.csi.siac.siacfin2ser.model.DatiSoggettoAllegato;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe di Action per la convalida del dettaglio dell'AllegatoAtto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 27/10/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(ConvalidaAllegatoAttoBaseAction.MODEL_SESSION_NAME)
public class ConvalidaAllegatoAttoDettaglioAction extends ConvalidaAllegatoAttoBaseAction {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4561331207382251492L;

	/**
	 * Preparazione per il metodo {@link #ottieniDettaglioElenco()}.
	 */
	public void prepareOttieniDettaglioElenco() {
		model.setElencoDocumentiAllegato(null);
	}
	
	/**
	 * Ottiene il dettaglio dell'elenco selezionato.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione.
	 */
	public String ottieniDettaglioElenco() {
		final String methodName = "ottieniDettaglioElenco";
		RicercaDettaglioElencoResponse response;
		try {
			response = ottieniResponseRicercaDettaglioElenco();
		} catch (WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Errore nell'invocazione del servizio: " + wsife.getMessage());
			return INPUT;
		}
		
		ElencoDocumentiAllegato eda = response.getElencoDocumentiAllegato();
		model.setElencoDocumentiAllegato(eda);
		
		// Ho l'elenco. Lo spezzo e lo wrappo
		List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> listaWrapper = ottieniWrapperPerElenco(eda);
		impostaSubdocumentiConvalidabiliENonConvalidabili(listaWrapper);
		log.debug(methodName, "Caricamento avvenuto con successo per l'elenco " + model.getElencoDocumentiAllegato().getUid()
				+ " - Subdocumenti convalidabili: " + model.getListaSubdocumentiConvalidabili().size()
				+ " - Subdocumenti non convalidabili: " + model.getListaSubdocumentiNonConvalidabili().size());
		
		return SUCCESS;
	}
	
	/**
	 * Ottiene la response per il servizio di ricerca di dettaglio dell'elenco.
	 * 
	 * @return la response del servizio
	 * 
	 * @throws WebServiceInvocationFailureException nel caso di fallimento nell'invocazione del servizio
	 */
	private RicercaDettaglioElencoResponse ottieniResponseRicercaDettaglioElenco() throws WebServiceInvocationFailureException {
		RicercaDettaglioElenco request = model.creaRequestRicercaDettaglioElenco();
		logServiceRequest(request);
		RicercaDettaglioElencoResponse response = allegatoAttoService.ricercaDettaglioElenco(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
		}
		return response;
	}
	
	/**
	 * Calcola il wrapper per l'elenco indicato.
	 * 
	 * @param eda l'elenco da wrappare
	 * @return il wrapper
	 */
	private List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> ottieniWrapperPerElenco(ElencoDocumentiAllegato eda) {
		String methodName = "ottieniWrapperPerElenco";
		List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> result = new ArrayList<ElementoElencoDocumentiAllegato<?,?,?,?,?>>();
		List<DatiSoggettoAllegato> listaDatiSoggettoAllegato = model.getAllegatoAtto().getDatiSoggettiAllegati();
		
		List<Subdocumento<?, ?>> subdocumenti = eda.getSubdocumenti();
		for(Subdocumento<?, ?> sub : subdocumenti) {
			Documento<?, ?> doc = sub.getDocumento();
			if(doc == null || doc.getSoggetto() == null) {
				// Non ho i dati del documento. Strano. Continuo
				continue;
			}
			DatiSoggettoAllegato dsa = ottieniDatiSoggettoAllegatoViaSoggetto(listaDatiSoggettoAllegato, doc.getSoggetto());
			ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?> eeda = null;
			if(sub instanceof SubdocumentoSpesa) {
				log.debug(methodName, "aggiungo subdocumento spesa: " + sub.getUid());
				eeda = new ElementoElencoDocumentiAllegatoSpesa(eda, dsa, model.isGestioneUEB(), (SubdocumentoSpesa) sub);
			} else if (sub instanceof SubdocumentoEntrata) {
				log.debug(methodName, "aggiungo subdocumento entrata: " + sub.getUid());
				eeda = new ElementoElencoDocumentiAllegatoEntrata(eda, dsa, model.isGestioneUEB(), (SubdocumentoEntrata) sub);
			}
			result.add(eeda);
		}
		log.debug(methodName, "returning size " + result.size() );
		return result;
	}

	/**
	 * Ottiene i datiSoggettoAllegato a partire dal soggetto corrispondente.
	 * 
	 * @param listaDatiSoggettoAllegato la lista dei dati
	 * @param soggetto                  il soggetto da ricercare
	 * 
	 * @return i dati relativi al soggetto, se presenti
	 */
	private DatiSoggettoAllegato ottieniDatiSoggettoAllegatoViaSoggetto(List<DatiSoggettoAllegato> listaDatiSoggettoAllegato, Soggetto soggetto) {
		for (DatiSoggettoAllegato dsa : listaDatiSoggettoAllegato) {
			if (soggetto.getUid() != 0 && dsa.getSoggetto() != null && soggetto.getUid() == dsa.getSoggetto().getUid()) {
				return dsa;
			}
		}
		return null;
	}
	
	/**
	 * Popola le tabelle dei subdocumenti convalidabili e non convalidabili.
	 * 
	 * @param list la lista tramite cui effettuare i popolamenti
	 */
	private void impostaSubdocumentiConvalidabiliENonConvalidabili(List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> list) {
		String methodName = "impostaSubdocumentiConvalidabiliENonConvalidabili";
		List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> convalidabili = new ArrayList<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>>();
		List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> convalidati = new ArrayList<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>>();
		List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> nonConvalidabiliACopertura = new ArrayList<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>>();
		
		boolean isUtenteQuietanza = isUtenteProfilatoQuietanza();
		
		for(ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?> eeda : list) {
			if(isNonConvalidabileACoperturaSubdocumento(eeda, isUtenteQuietanza)) {
				log.debug(methodName, "subdocumento. " + eeda.getUid() + " e' non convalidabile a copertura");
				nonConvalidabiliACopertura.add(eeda);
			} else if(isConvalidato(eeda)) {
				log.debug(methodName, "subdocumento. " + eeda.getUid() + " e' convalidato");
				convalidati.add(eeda);
			} else {
				log.debug(methodName, "subdocumento. " + eeda.getUid() + " e' convalidabile");
				convalidabili.add(eeda);
			}
		}
		log.debug(methodName, "Lista convalidabili size: " + convalidabili.size()); 
		// Imposto nel model
		model.setListaSubdocumentiConvalidabili(convalidabili);
		model.setListaSubdocumentiConvalidati(convalidati);
		model.setListaSubdocumentiACopertura(nonConvalidabiliACopertura);
		// Computo i totali
		model.computeTotaliSubdocumenti();
	}
	
	/**
	 * Controlla se l'utente sia profilato per la quietanza.
	 * 
	 * @return <code>true</code> se l'utente &eacute; profilato per la quietanza; <code>false</code> in caso contrario.
	 */
	private boolean isUtenteProfilatoQuietanza() {
		return AzioniConsentiteFactory.isConsentito(AzioniConsentite.ALLEGATO_ATTO_CONVALIDA_QUIETANZA, sessionHandler.getAzioniConsentite());
	}
	
	/**
	 * Controlla se il subdocumento sia convalidabile o meno.
	 * 
	 * @param eeda il wrapper del subdocumento da controllare
	 * @param isQuietanza se l'utente sia abilitato alle azioni della qietanza
	 * 
	 * @return <code>true</code> se il subdocumento &eacute; non convalidabile; <code>false</code> in caso contrario
	 */
	private <E extends ElementoElencoDocumentiAllegato<?, ?, ?, ?, S>, S extends Subdocumento<?, ?>> boolean isNonConvalidabileACoperturaSubdocumento(E eeda, boolean isQuietanza) {
		S subdocumento = eeda.extractSubdocumento();
		// Se non ho la parte di quietanza, ignoro il controllo
		return isQuietanza && subdocumento.getFlagConvalidaManuale() != null && !Boolean.FALSE.equals(subdocumento.getFlagACopertura());
	}
	
	/**
	 * Controlla se il subdocumento sia convalidato.
	 * 
	 * @param eeda il wrapper del subdocumento da controllare
	 * 
	 * @return <code>true</code> se il subdocumento &eacute; convalidato; <code>false</code> in caso contrario
	 */
	private <E extends ElementoElencoDocumentiAllegato<?, ?, ?, ?, S>, S extends Subdocumento<?, ?>> boolean isConvalidato(E eeda) {
		return eeda.extractSubdocumento().getFlagConvalidaManuale() != null;
	}
	
	/**
	 * Validazione per il metodo {@link #dettaglioElenco()}.
	 */
	public void validateOttieniDettaglioElenco() {
		checkCondition(model.getElencoDocumentiAllegato() != null && model.getElencoDocumentiAllegato().getUid() != 0,
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Elenco"));
	}
	
	/**
	 * Visualizzazione del dettaglio dell'elenco.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione.
	 */
	public String dettaglioElenco() {
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #convalidaSubdocumento()}.
	 */
	public void prepareConvalidaSubdocumento() {
		model.setListaUid(null);
	}
	
	/**
	 * Convalida del subdocumento fornito in input.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String convalidaSubdocumento() {
		final String methodName = "convalidaSubdocumento";
		
		ConvalidaAllegatoAttoPerElenchi request = model.creaRequestConvalidaAllegatoAttoPerElenchiPerSubdocumenti();
		logServiceRequest(request);
		
		// SIAC-5575: l'operazione asincrona deve essere sotto nome 'CONVALIDA'
		AzioneRichiesta azioneRichiesta = AzioniConsentite.ALLEGATO_ATTO_CONVALIDA.creaAzioneRichiesta(sessionHandler.getAzioniConsentite());
		AsyncServiceResponse response = allegatoAttoService.convalidaAllegatoAttoPerElenchiAsync(wrapRequestToAsync(request, azioneRichiesta));
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		log.info(methodName, "Inizio invocazione asincrona avvenuta con successo per i subdocumenti " + model.getListaUid());
		addInformazione(ErroreCore.ELABORAZIONE_ASINCRONA_AVVIATA.getErrore("Convalida subdocumenti", ""));
		// Ricarica dati dell'elenco
		return ottieniDettaglioElenco();
	}
	
	/**
	 * Validazione per il metodo {@link #convalidaSubdocumento()}.
	 */
	public void validateConvalidaSubdocumento() {
		checkCondition(!model.getListaUid().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("quota da convalidare"));
	}
	
}
