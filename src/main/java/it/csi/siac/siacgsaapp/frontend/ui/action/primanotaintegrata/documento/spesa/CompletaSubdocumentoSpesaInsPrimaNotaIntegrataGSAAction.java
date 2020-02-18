/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.primanotaintegrata.documento.spesa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.documento.spesa.InserisciPrimaNotaIntegrataDocumentoSpesaBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.documento.spesa.InserisciPrimaNotaIntegrataSubdocumentoSpesaBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.selector.CausaleEPGSASelector;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.selector.CausaleEPSelector;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoScritturaPrimaNotaIntegrata;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoScritturaPrimaNotaIntegrataFactory;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacgenser.frontend.webservice.ConciliazioneService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasseResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.documento.spesa.CompletaDocumentoSpesaInsPrimaNotaIntegrataGSAModel;

/**
 * Classe di action per l'inserimento della prima integrata per il subdocumento di spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/05/2015
 * @version 1.0.1 - 19/05/2015 - gestione GSA/GSA
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(InserisciPrimaNotaIntegrataDocumentoSpesaBaseAction.MODEL_COMPLETA_DOCUMENTO_SPESA_GSA)
public class CompletaSubdocumentoSpesaInsPrimaNotaIntegrataGSAAction extends InserisciPrimaNotaIntegrataSubdocumentoSpesaBaseAction<CompletaDocumentoSpesaInsPrimaNotaIntegrataGSAModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2703718959865123005L;
	
	@Autowired
	private transient ConciliazioneService conciliazioneService;

	@Override
	protected BilSessionParameter getSessionParameterListaCausaleEPIntegrata() {
		return BilSessionParameter.LISTA_CAUSALE_EP_INTEGRATA_GSA;
	}

	@Override
	protected CausaleEPSelector istanziaSelettoreCausale() {
		return new CausaleEPGSASelector();
	}
	
	/**
	 * Proposta dei conti per il movimento EP a partire dalla causale EP selezionata.
	 * 
	 * @return una stringa corrispondente dal risultato dell'invocazione
	 */
	@Override
	public String proponiContiDaCausaleEP() {
		final String methodName = "proponiContiDaCausaleEP";
		getDatiCausaleDaLista();
		CausaleEP causaleEP = model.getCausaleEP();
		
		if(causaleEP == null || causaleEP.getUid() == 0) {
			// Se non ho caricato la causale, svuoto i conti
			log.debug(methodName, "Nessuna causale EP selezionata");
			model.setListaElementoScrittura(new ArrayList<ElementoScritturaPrimaNotaIntegrata>());
			model.setListaElementoScritturaPerElaborazione(new ArrayList<ElementoScritturaPrimaNotaIntegrata>());
			calcolaTotaleDareAvere();
			return SUCCESS;
		}
		
		List<ElementoScritturaPrimaNotaIntegrata> listaElementoScrittura = ottieniListaElementoScritturaPNI(causaleEP);
		
		List<ElementoScritturaPrimaNotaIntegrata> listaElementoScritturaPerElaborazione = ReflectionUtil.deepClone(listaElementoScrittura);
		
		log.debug(methodName, "trovate " +listaElementoScritturaPerElaborazione.size() + " scritture");
		
		impostaImportiScritture(listaElementoScritturaPerElaborazione);
		
		model.setListaElementoScrittura(listaElementoScrittura);
		model.setListaElementoScritturaPerElaborazione(listaElementoScritturaPerElaborazione);
		
		log.debug(methodName, "trovate " +model.getListaElementoScritturaPerElaborazione().size() + " scritture");
		
		// Ricalcolo Dare e avere
		calcolaTotaleDareAvere();
		
		log.debug(methodName, "trovate " +model.getListaElementoScritturaPerElaborazione().size() + " scritture");
		
		
		
		return SUCCESS;
	}
	
	/**
	* Ottiene la lista dei wrapper delle scritture da visualizzare.
	* @param causaleEP la causale
	* @return la lista creata
	*/
	private List<ElementoScritturaPrimaNotaIntegrata> ottieniListaElementoScritturaPNI(CausaleEP causaleEP) {
		if(model.getMovimentoEP() != null && model.getMovimentoEP().getUid() != 0 && model.getCausaleEPOriginaria() != null && model.getCausaleEPOriginaria().getUid() == causaleEP.getUid()) {
			return ElementoScritturaPrimaNotaIntegrataFactory.creaListaScrittureDaSingoloMovimentoEP(model.getMovimentoEP(),
					causaleEP.getContiTipoOperazione() != null && !causaleEP.getContiTipoOperazione().isEmpty());
		}
		
		Map<ClasseDiConciliazione,List<Conto>> mappaConti = new HashMap<ClasseDiConciliazione, List<Conto>>();
		for(ContoTipoOperazione ctop : model.getCausaleEP().getContiTipoOperazione()){
			if(ctop.getClasseDiConciliazione() != null){
				RicercaContiConciliazionePerClasse req = model.creaRequestRicercaContiConciliazionePerClasse(ctop.getClasseDiConciliazione());
				RicercaContiConciliazionePerClasseResponse res = conciliazioneService.ricercaContiConciliazionePerClasse(req);
				mappaConti.put(ctop.getClasseDiConciliazione(), res.getConti());
				model.setClassificatoreGerarchico(model.getSubdocumento().getImpegno().getCapitoloUscitaGestione().getMacroaggregato());
			}
		}
		
		return mappaConti.isEmpty()? 
				ElementoScritturaPrimaNotaIntegrataFactory.creaListaScrittureDaCausaleEP(model.getCausaleEP(), BigDecimal.ZERO)
				: ElementoScritturaPrimaNotaIntegrataFactory.creaListaScrittureDaCausaleEPEContiConciliazione(model.getCausaleEP(), BigDecimal.ZERO, mappaConti);
	}
}
