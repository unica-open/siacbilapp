/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documentoiva;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.documentoiva.RicercaDocumentoIvaSpesaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumentoResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;

/**
 * Classe di action per la ricerca del Documento IVA di Spesa
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 16/06/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaDocumentoIvaSpesaAction extends GenericDocumentoIvaSpesaAction<RicercaDocumentoIvaSpesaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5395273509464490264L;
	
	@Autowired private transient DocumentoService documentoService;
	@Autowired private transient SoggettoService soggettoService;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		
		// Caricamento liste
		try {	
			caricaListaTipoDocumento();
			caricaListaTipoRegistrazioneIva();
			caricaListaTipoRegistroIva();
			caricaListaAttivitaIva();
			caricaListaRegistroIva();
			caricaListaClasseSoggetto();
		} catch(WebServiceInvocationFailureException e) {
			log.error("prepare", "Errore nell'invocazione del caricamento di una lista: " + e.getMessage());
		} finally {
			checkPrepareConclusoSenzaErrori();
		}
	}
	
	/**
	 * Controlla se il metodo prepare si sia concluso senza alcun errore. In caso contrario, rilancia un'eccezione per uscire dalla pagina.
	 * 
	 * @throws GenericFrontEndMessagesException in caso di errori nei servizii
	 */
	protected void checkPrepareConclusoSenzaErrori() {
		if(hasErrori()) {
			StringBuilder erroriRiscontrati = new StringBuilder();
			for(Errore errore : model.getErrori()) {
				erroriRiscontrati.append(errore.getTesto() + "\n");
			}
			
			throw new GenericFrontEndMessagesException(ErroreCore.ERRORE_DI_SISTEMA.getErrore(erroriRiscontrati.toString()).getTesto(),
					GenericFrontEndMessagesException.Level.ERROR);
		}
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * Carica la lista delle classi del soggetto.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio fallisca
	 */
	protected void caricaListaClasseSoggetto() throws WebServiceInvocationFailureException {
		List<CodificaFin> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO);
		if(listaInSessione == null) {
			ListeGestioneSoggetto request = model.creaRequestListeGestioneSoggetto();
			ListeGestioneSoggettoResponse response = soggettoService.listeGestioneSoggetto(request);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException("caricaListaClasseSoggetto");
			}
			
			listaInSessione = response.getListaClasseSoggetto();
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO, listaInSessione);
		}
		
		model.setListaClasseSoggetto(listaInSessione);
	}
	
	/**
	 * Ricerca i SubdocumentoIva.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ricerca() {
		final String methodName = "ricerca";
		
		RicercaSinteticaSubdocumentoIvaSpesa request = model.creaRequestRicercaSinteticaSubdocumentoIva();
		logServiceRequest(request);
		RicercaSinteticaSubdocumentoIvaSpesaResponse response = documentoIvaSpesaService.ricercaSinteticaSubdocumentoIvaSpesa(request);
		logServiceResponse(response);
		
		if (response.hasErrori()) {
			log.info(methodName, "Fallimento nella chiamata al servizio di ricerca sintetica");
			addErrori(response);
			return INPUT;
		}
		
		int totaleElementi = response.getListaSubdocumentoIvaSpesa().getTotaleElementi();
		
		if(totaleElementi == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo. Totale preDocumenti: " + totaleElementi);
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_DOCUMENTI_IVA_SPESA, request);

		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_DOCUMENTI_IVA_SPESA, response.getListaSubdocumentoIvaSpesa());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per la ricerca del DocumentoIva.
	 */
	public void validateRicerca() {
		DocumentoSpesa d = model.getDocumento();
		
		boolean formValido =
				checkPresenzaIdEntita(model.getTipoDocumento())
				|| (d != null
					&& (checkCampoValorizzato(d.getAnno(), "Anno documento")
						|| checkStringaValorizzata(d.getNumero(), "Numero documento")
						|| checkCampoValorizzato(d.getDataEmissione(), "Data emissione")))
				|| checkStringaValorizzata(model.getFlagIntracomunitario(), "Documento intracomunitario")
				|| checkCampoValorizzato(model.getProgressivoIvaDa(), "Progressivo IVA da")
				|| checkCampoValorizzato(model.getProgressivoIvaA(), "Progressivo IVA a")
				|| checkCampoValorizzato(model.getTipoRegistroIva(), "Tipo registro IVA")
				|| checkPresenzaIdEntita(model.getTipoRegistrazioneIva())
				|| checkPresenzaIdEntita(model.getAttivitaIva())
				|| checkStringaValorizzata(model.getFlagRilevanteIrap(), "Flag Rilevante IRAP")
				|| checkPresenzaIdEntita(model.getRegistroIva())
				
				|| checkCampoValorizzato(model.getNumeroProtocolloProvvisorioDa(), "Protocollo provvisorio da")
				|| checkCampoValorizzato(model.getNumeroProtocolloProvvisorioA(), "Protocollo provvisorio a")
				|| checkCampoValorizzato(model.getDataProtocolloProvvisorioDa(), "Data Protocollo provvisorio da")
				|| checkCampoValorizzato(model.getDataProtocolloProvvisorioA(), "Data Protocollo provvisorio a")
				
				|| checkCampoValorizzato(model.getNumeroProtocolloDefinitivoDa(), "Protocollo Definitivo da")
				|| checkCampoValorizzato(model.getNumeroProtocolloDefinitivoA(), "Protocollo Definitivo a")
				|| checkCampoValorizzato(model.getDataProtocolloDefinitivoDa(), "Data Protocollo Definitivo da")
				|| checkCampoValorizzato(model.getDataProtocolloDefinitivoA(), "Data Protocollo Definitivo a");
				
		boolean soggettoValido = validazioneSoggettoEPopolamentoUid();
		 
		formValido = formValido || soggettoValido;
		
		if(!formValido) {
			addErrore(ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		}
	}
	

	
	/**
	 * Effettua una validazione del Soggetto fornito in input.
	 * 
	 * @return <code>true</code> se la validazione &eacute; andata a buon fine; <code>false</code> in caso contrario
	 */
	protected boolean validazioneSoggettoEPopolamentoUid() {
		final String methodName = "validazioneSoggettoEPopolamentoUid";
		Soggetto soggetto = model.getSoggetto();
		// Se non ho il soggetto, sono a posto
		if(soggetto == null || StringUtils.isBlank(soggetto.getCodiceSoggetto())) {
			return false;
		}
		
		RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave(soggetto);
		logServiceRequest(request);
		RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
		logServiceResponse(response);
		
		// Se ho errori, esco subito dopo aver caricato gli errori
		if(response.hasErrori()) {
			addErrori(response);
			return false;
		}
		
		if(response.getSoggetto() == null) {
			log.info(methodName, "Nessun soggetto fornito dal servizio");
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Il soggetto non e' presente"));
			return false;
		}
		
		soggetto = response.getSoggetto();
		// Aggiorno i dati del soggetto
		model.setSoggetto(soggetto);
		
		checkCondition(!StatoOperativoAnagrafica.ANNULLATO.equals(soggetto.getStatoOperativo()),
				ErroreFin.SOGGETTO_ANNULLATO.getErrore());
		checkCondition(!StatoOperativoAnagrafica.BLOCCATO.equals(soggetto.getStatoOperativo()),
				ErroreFin.SOGGETTO_BLOCCATO.getErrore());
		return true;
	}

	/**
	 * Carica la lista dei tipi di documento.
	 */
	private void caricaListaTipoDocumento() {
		List<TipoDocumento> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_DOCUMENTO);
		if(listaInSessione == null) {
			RicercaTipoDocumento request = model.creaRequestRicercaTipoDocumento(TipoFamigliaDocumento.SPESA, null, null);
			logServiceRequest(request);
			RicercaTipoDocumentoResponse response = documentoService.ricercaTipoDocumento(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				return;
			}
			
			listaInSessione = response.getElencoTipiDocumento();
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_DOCUMENTO, listaInSessione);
			
		}
		model.setListaTipoDocumento(listaInSessione);
	}
	
}
