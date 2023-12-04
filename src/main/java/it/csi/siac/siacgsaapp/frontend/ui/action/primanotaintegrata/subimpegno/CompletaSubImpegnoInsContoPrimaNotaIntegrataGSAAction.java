/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.primanotaintegrata.subimpegno;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.subimpegno.GestioneSubImpegnoInsContoPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.subimpegno.GestioneSubImpegnoInsPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoScritturaPrimaNotaIntegrata;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoScritturaPrimaNotaIntegrataFactory;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacgenser.frontend.webservice.ConciliazioneService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasseResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.subimpegno.CompletaSubImpegnoInsPrimaNotaIntegrataGSAModel;

/**
 * Classe di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, impegno. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/10/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(GestioneSubImpegnoInsPrimaNotaIntegrataBaseAction.MODEL_SESSION_NAME_COMPLETA_INS_SUBIMPEGNO_GSA)
public class CompletaSubImpegnoInsContoPrimaNotaIntegrataGSAAction extends GestioneSubImpegnoInsContoPrimaNotaIntegrataBaseAction <CompletaSubImpegnoInsPrimaNotaIntegrataGSAModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -937344455245105911L;
	
	@Autowired
	private transient ConciliazioneService conciliazioneService;

	@Override
	protected BilSessionParameter getSessionParameterListaCausaleEPIntegrata() {
		return BilSessionParameter.LISTA_CAUSALE_EP_INTEGRATA_GSA;
	}
	
	/**
	 * Ottiene la lista dei conti da associare alla prima nota.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@Override
	public String ottieniListaConti() {
		getDatiCausaleDaLista();
		CausaleEP causaleEP = model.getCausaleEP();
		boolean contiCausaleNonValorizzati = causaleEP == null || causaleEP.getUid() == 0 || causaleEP.getContiTipoOperazione() == null || causaleEP.getContiTipoOperazione().isEmpty();
		if(contiCausaleNonValorizzati) {
			model.setListaElementoScrittura(new ArrayList<ElementoScritturaPrimaNotaIntegrata>());
			model.setListaElementoScritturaPerElaborazione(new ArrayList<ElementoScritturaPrimaNotaIntegrata>());
			model.setContiCausale(false);
			return SUCCESS;
		}
		
		model.setContiCausale(true);
		
		List<ElementoScritturaPrimaNotaIntegrata> listaElemScritturaPNI = ottieniListaElementoScritturaPNI();
		
		model.setListaElementoScrittura(listaElemScritturaPNI);
		List<ElementoScritturaPrimaNotaIntegrata> clone = ReflectionUtil.deepClone(model.getListaElementoScrittura());
		model.setListaElementoScritturaPerElaborazione(clone);
		ricalcolaTotali();
		return SUCCESS;
	}


	/**
	* Ottiene la lista dei wrapper delle scritture da visualizzare.
	* @return la lista creata 
	*/
	private List<ElementoScritturaPrimaNotaIntegrata> ottieniListaElementoScritturaPNI() {
		Map<ClasseDiConciliazione,List<Conto>> mappaConti = new HashMap<ClasseDiConciliazione, List<Conto>>();
		for(ContoTipoOperazione ctop : model.getCausaleEP().getContiTipoOperazione()){
			if(ctop.getClasseDiConciliazione() != null){
				RicercaContiConciliazionePerClasse req = model.creaRequestRicercaContiConciliazionePerClasse(ctop.getClasseDiConciliazione());
				RicercaContiConciliazionePerClasseResponse res = conciliazioneService.ricercaContiConciliazionePerClasse(req);
				mappaConti.put(ctop.getClasseDiConciliazione(), res.getConti());
				model.setClassificatoreGerarchico(model.getImpegno().getCapitoloUscitaGestione().getMacroaggregato());
			}
		}
		
		return mappaConti.isEmpty()? 
				ElementoScritturaPrimaNotaIntegrataFactory.creaListaScrittureDaCausaleEP(model.getCausaleEP(), getImportoMovimento())
				: ElementoScritturaPrimaNotaIntegrataFactory.creaListaScrittureDaCausaleEPEContiConciliazione(model.getCausaleEP(), getImportoMovimento(), mappaConti);
	}

}

