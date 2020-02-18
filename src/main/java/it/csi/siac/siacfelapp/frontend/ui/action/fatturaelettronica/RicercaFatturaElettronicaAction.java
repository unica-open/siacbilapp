/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfelapp.frontend.ui.action.fatturaelettronica;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.ValidationUtil;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfelapp.frontend.ui.model.fatturaelettronica.RicercaFatturaElettronicaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodicePCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodicePCCResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceUfficioDestinatarioPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceUfficioDestinatarioPCCResponse;
import it.csi.siac.siacfin2ser.model.CodicePCC;
import it.csi.siac.siacfin2ser.model.CodiceUfficioDestinatarioPCC;
import it.csi.siac.sirfelser.frontend.webservice.FatturaElettronicaService;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaSinteticaFatturaElettronica;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaSinteticaFatturaElettronicaResponse;
import it.csi.siac.sirfelser.model.FatturaFEL;
import it.csi.siac.sirfelser.model.StatoAcquisizioneFEL;
import it.csi.siac.sirfelser.model.TipoDocumentoFEL;

/**
 * Classe di action per la ricerca della fattura elettronica.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/06/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaFatturaElettronicaAction extends GenericBilancioAction<RicercaFatturaElettronicaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4736186503577124881L;
	
	@Autowired private transient DocumentoService documentoService;
	@Autowired private transient FatturaElettronicaService fatturaElettronicaService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		
		// Caricamento liste
		try {
			caricaListaTipoDocumentoFEL();
			caricaListaUfficioPCC();
			caricaListaStatoAcquisizioneDocumento();
			
			// Caricamento codicePCC
			caricaCodicePCC();
		} catch (WebServiceInvocationFailureException wsife) {
			throw new GenericFrontEndMessagesException(wsife.getMessage(), wsife);
		}
	}
	
	/**
	 * Caricamento della lista del tipo di documento FEL
	 */
	private void caricaListaTipoDocumentoFEL() {
		model.setListaTipoDocumentoFEL(Arrays.asList(TipoDocumentoFEL.values()));
	}

	/**
	 * Caricamento della lista dell'ufficio destinatario.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	private void caricaListaUfficioPCC() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaUfficioPCC";
		
		// Controllo se ho il dato in sessione
		List<CodiceUfficioDestinatarioPCC> listaUfficioPCC = sessionHandler.getParametro(BilSessionParameter.LISTA_CODICE_UFFICIO_DESTINATARIO_PCC);
		//List<StrutturaAmministrativoContabile> listaSAC = ottieniListaStruttureAmministrativoContabiliDaSessione();
		List<StrutturaAmministrativoContabile> listaSAC = new ArrayList<StrutturaAmministrativoContabile>();
		
		if(listaUfficioPCC == null) {
			// Carico da servizio
			log.debug(methodName, "Lista UfficioPCC non ancora presente in sessione. Caricamento da servizio");
			
			RicercaCodiceUfficioDestinatarioPCC request = model.creaRequestRicercaUfficioPCC(listaSAC);
			logServiceRequest(request);
			RicercaCodiceUfficioDestinatarioPCCResponse response = documentoService.ricercaCodiceUfficioDestinatarioPCC(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				String errorMsg = createErrorInServiceInvocationString(request, response);
				log.info(methodName, errorMsg);
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			
			listaUfficioPCC = response.getCodiciUfficiDestinatariPcc();
			sessionHandler.setParametro(BilSessionParameter.LISTA_CODICE_UFFICIO_DESTINATARIO_PCC, listaUfficioPCC);
		}
		model.setListaCodiceUfficioDestinatarioPCC(listaUfficioPCC);
	}
	
	/**
	 * Calcola il primo ufficio corrispondente alla una SAC dell'operatore.
	 * 
	 * @param sac le SAC per cui fltrare
	 */
	private void preimpostaCodiceUfficioPCCCorrispondenteAllaSAC(Iterable<StrutturaAmministrativoContabile> sac) {
		List<CodiceUfficioDestinatarioPCC> uffici = model.getListaCodiceUfficioDestinatarioPCC();
		for(CodiceUfficioDestinatarioPCC c : uffici) {
			if(c.getStrutturaAmministrativoContabile() == null || c.getStrutturaAmministrativoContabile().getUid() == 0) {
				continue;
			}
			for(StrutturaAmministrativoContabile s : sac) {
				if(s.getUid() == c.getStrutturaAmministrativoContabile().getUid()) {
					// Preimposto il dato
					if(model.getFatturaFEL() == null) {
						model.setFatturaFEL(new FatturaFEL()); 
					}
					model.getFatturaFEL().setCodiceDestinatario(c.getCodice());
					return;
				}
			}
		}
	}
	
	/**
	 * Preimposta il primo codice PCC corrispondente alla una SAC dell'operatore.
	 * 
	 * @param sac le SAC per cui fltrare
	 */
	private void preimpostaCodicePCCCorrispondenteAllaSAC(Iterable<StrutturaAmministrativoContabile> sac) {
		List<CodicePCC> codici = model.getListaCodicePCC();
		for(CodicePCC c : codici) {
			if(c.getStrutturaAmministrativoContabile() == null || c.getStrutturaAmministrativoContabile().getUid() == 0) {
				continue;
			}
			for(StrutturaAmministrativoContabile s : sac) {
				if(s.getUid() == c.getStrutturaAmministrativoContabile().getUid()) {
					model.setCodicePCC(c);
					return;
				}
			}
		}
	}
	
	/**
	 * Caricamento della lista dello stato acquisizione del documento.
	 */
	private void caricaListaStatoAcquisizioneDocumento() {
		model.setListaStatoAcquisizioneFEL(Arrays.asList(StatoAcquisizioneFEL.values()));
	}

	/**
	 * Caricamento del codice PCC dell'operatore, se presente
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	private void caricaCodicePCC() throws WebServiceInvocationFailureException {
		final String methodName = "caricaCodicePCC";
		
		// Controllo se ho il dato in sessione
		List<CodicePCC> listaCodicePCC = sessionHandler.getParametro(BilSessionParameter.LISTA_CODICE_PCC);
		//List<StrutturaAmministrativoContabile> listaSAC = ottieniListaStruttureAmministrativoContabiliDaSessione();
		List<StrutturaAmministrativoContabile> listaSAC = new ArrayList<StrutturaAmministrativoContabile>();
		
		if(listaCodicePCC == null) {
			// Carico da servizio
			log.debug(methodName, "Lista CodicePCC non ancora presente in sessione. Caricamento da servizio");
			
			RicercaCodicePCC request = model.creaRequestRicercaCodicePCC(listaSAC);
			logServiceRequest(request);
			RicercaCodicePCCResponse response = documentoService.ricercaCodicePCC(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				String errorMsg = createErrorInServiceInvocationString(request, response);
				log.info(methodName, errorMsg);
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			
			listaCodicePCC = response.getCodiciPCC();
			sessionHandler.setParametro(BilSessionParameter.LISTA_CODICE_PCC, listaCodicePCC);
		}
		model.setListaCodicePCC(listaCodicePCC);
		
		// SIAC-3667: non preimposto questo campo in questo modo
		// Imposto il primo se presente
//		if(!listaCodicePCC.isEmpty()) {
//			CodicePCC codicePCC = listaCodicePCC.get(0);
//			model.setCodicePCC(codicePCC);
//		}
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// SIAC-3667: preimposto codice PCC e ufficio PCC
		List<StrutturaAmministrativoContabile> listaSAC = ottieniListaStruttureAmministrativoContabiliDaSessione();
		
		preimpostaCodiceUfficioPCCCorrispondenteAllaSAC(listaSAC);
		preimpostaCodicePCCCorrispondenteAllaSAC(listaSAC);
		
		return SUCCESS;
	}
	
	/**
	 * Ricerca della fattura elettronica in base ai dati forniti.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String effettuaRicerca() {
		final String methodName = "effettuaRicerca";
		
		RicercaSinteticaFatturaElettronica request = model.creaRequestRicercaSinteticaFatturaElettronica();
		logServiceRequest(request);
		RicercaSinteticaFatturaElettronicaResponse response = fatturaElettronicaService.ricercaSinteticaFatturaElettronica(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		// Non ho errori. Devo ancora controllare di avere risultati
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessuna fattura elettronica corrispondente ai dati forniti");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo. Totale elementi trovati: " + response.getTotaleElementi());
		
		// Imposto in sessione i dati
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_FATTURA_FEL, request);
		
		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_FATTURA_FEL, response.getFattureFEL());
		
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #effettuaRicerca()}.
	 */
	public void validateEffettuaRicerca() {
		checkNotNull(model.getFatturaFEL(), "Fattura elettronica", true);
		
		FatturaFEL fatturaFEL = model.getFatturaFEL();
		
		boolean ricercaValida = fatturaFEL.getTipoDocumentoFEL() != null
				|| StringUtils.isNotBlank(model.getCodiceFiscale())
				|| StringUtils.isNotBlank(model.getPartitaIva())
				|| StringUtils.isNotBlank(fatturaFEL.getNumero())
				|| StringUtils.isNotBlank(fatturaFEL.getCodiceDestinatario())
				|| model.getDataFatturaDa() != null
				|| model.getDataFatturaA() != null
				|| fatturaFEL.getStatoAcquisizioneFEL() != null;
		
		// TODO: davvero?
		// Solo una tra codice fiscale e partita iva puo' essere valorizzata
		checkCondition(StringUtils.isBlank(model.getCodiceFiscale()) || StringUtils.isBlank(model.getPartitaIva()),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Codice fiscale e Partita iva", ": solo uno dei due campi puo' essere valorizzato"));
		
		checkCondition(StringUtils.isBlank(model.getCodiceFiscale()) || ValidationUtil.isValidCodiceFiscaleEvenTemporary(model.getCodiceFiscale()),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Codice fiscale", ": non e' sintatticamente valido"));
		checkCondition(StringUtils.isBlank(model.getPartitaIva()) || ValidationUtil.isValidPartitaIva(model.getPartitaIva()),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Partita iva", ": non e' sintatticamente valida"));
		
		// Se entrambe le date della fattura sono state valorizzate, la data 'da' deve essere uguale o minore la data 'a'
		checkCondition(model.getDataFatturaDa() == null || model.getDataFatturaA() == null || !model.getDataFatturaDa().after(model.getDataFatturaA()),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Data fattura", ": la data 'da' non puo' essere successiva alla data 'a'"));
		
		checkCondition(ricercaValida, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
	}
	
}
