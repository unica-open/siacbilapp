/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento.aggiornamento.entrata;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBilResponse;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacfin2app.frontend.ui.action.documento.GenericAggiornaDocumentoAction;
import it.csi.siac.siacfin2app.frontend.ui.model.documento.AggiornaDocumentoEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaEntrataService;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.frontend.webservice.ProvvisorioService;

/**
 * Classe di action base per l'aggiornamento del Documento di entrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/09/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(AggiornaDocumentoEntrataBaseAction.FAMILY_NAME)
public class AggiornaDocumentoEntrataBaseAction extends GenericAggiornaDocumentoAction<AggiornaDocumentoEntrataModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 2216228847988229357L;
	
	/** Nome della famiglia */
	public static final String FAMILY_NAME = "AggiornaDocumentoEntrata";

	/** Serviz&icirc; del documento di entrata */
	@Autowired protected transient DocumentoEntrataService documentoEntrataService;
	/** Serviz&icirc; del documento iva di entrata */
	@Autowired protected transient DocumentoIvaEntrataService documentoIvaEntrataService;
	/** Serviz&icirc; dei classificatori bil */
	@Autowired protected transient ClassificatoreBilService classificatoreBilService;
	/** Serviz&icirc; del provvisorio */
	@Autowired protected transient ProvvisorioService provvisorioService;

	@Override
	public void prepare() throws Exception {
		cleanErroriMessaggiInformazioni();
	}
	
	/**
	 * Inizializza la action.
	 * 
	 * @throws FrontEndBusinessException nel caso in cui l'inizializzazione non vada a buon fine
	 */
	protected void initAction() throws FrontEndBusinessException {
		try {
			super.prepare();
		} catch (Exception e) {
			log.error("initAction", "Errore nell'inizializzazione della action: " + e.getMessage());
			throw new FrontEndBusinessException("Errore nell'inizializzazione della action", e);
		}
	}

	/**
	 * Carica la lista dei tipi di finanziamento per la quota.
	 */
	protected void checkAndObtainListaTipiFinanziamento() {
		List<TipoFinanziamento> listaTipoFinanziamento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_FINANZIAMENTO);
		if (listaTipoFinanziamento != null && !listaTipoFinanziamento.isEmpty()) {
			return;
		}

		LeggiClassificatoriGenericiByTipoElementoBil request = model
				.creaRequestLeggiClassificatoriGenericiByTipoElementoBil(BilConstants.CODICE_CAPITOLO_ENTRATA_GESTIONE.getConstant());
		LeggiClassificatoriGenericiByTipoElementoBilResponse response = classificatoreBilService.leggiClassificatoriGenericiByTipoElementoBil(request);

		listaTipoFinanziamento = response.getClassificatoriTipoFinanziamento();

		sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_FINANZIAMENTO, listaTipoFinanziamento);

		model.setListaTipiFinanziamento(listaTipoFinanziamento);
	}
	
	@Override
	protected void cleanModel() {
		super.cleanModel();
		model.setMovimentoGestione(null);
	}
	
	/**
	 * Verifico che le attivazioni contabili siano possibili
	 * 
	 */
	protected void checkAttivazioneRegContabili() {
		final String methodName = "checkAttivazioneRegContabili";
		if(model.getDocumento() == null || model.getDocumento().getTipoDocumento() == null) {
			log.debug(methodName, "Dati non presenti. Non attivo alcunche'");
			model.setAttivaRegistrazioniContabiliVisible(false);
			return;
		}
		
		StatoOperativoDocumento sto = model.getDocumento().getStatoOperativoDocumento();
		TipoDocumento td = model.getDocumento().getTipoDocumento();
		log.debug(methodName, "stato operativo" + sto);
		log.debug(methodName, "contabilizza " + model.getDocumento().getContabilizzaGenPcc());
		log.debug(methodName, "flag attiva gen " + td.getFlagAttivaGEN());
		boolean condizioneVisibilitaRegistrazioniContabili = !StatoOperativoDocumento.ANNULLATO.equals(sto)
				&& !StatoOperativoDocumento.EMESSO.equals(sto)
				&& !StatoOperativoDocumento.INCOMPLETO.equals(sto)
				&& !Boolean.TRUE.equals(model.getDocumento().getContabilizzaGenPcc())
				&& Boolean.TRUE.equals(td.getFlagAttivaGEN());
		model.setAttivaRegistrazioniContabiliVisible(condizioneVisibilitaRegistrazioniContabili);
	}
}
