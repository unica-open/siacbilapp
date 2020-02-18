/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto.associa.movimento;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto.GenericAssociaAllegatoAttoAction;
import it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.AssociaMovimentoAllegatoAttoModel;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.ProvvisorioService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiaveResponse;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.soggetto.ClassificazioneSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe base di Action per l'associazione tra movimento e allegato atto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2014
 */
public class AssociaMovimentoAllegatoAttoBaseAction extends GenericAssociaAllegatoAttoAction<AssociaMovimentoAllegatoAttoModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7062420998694051866L;

	/** Nome del model per la sessione */
	protected static final String MODEL_SESSION_NAME = "AssociaMovimentoAllegatoAtto";
	
	/** Serviz&icirc; del movimento di gestione */
	@Autowired protected transient MovimentoGestioneService movimentoGestioneService;

	/** Serviz&icirc; del provvisorio di cassa*/
	@Autowired protected transient ProvvisorioService provvisorioService;
	
	@Override
	public void prepare() throws Exception {
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		// Pulisco il model
		setModel(null);
		super.prepare();
	}
	
	/**
	 * Controlla la coerenza tra soggetto e movimento di gestione.
	 * 
	 * @param soggetto   il soggetto da controllare
	 * @param movimento  il movimento da controllare
	 * @param tipo       il tipo del movimento
	 * @param proseguire se proseguire con l'elaborazione a seguito della discrepanza tra classi
	 */
	protected void checkCoerenzaSoggettoMovimento(Soggetto soggetto, MovimentoGestione movimento, String tipo, Boolean proseguire) {
		if(movimento.getSoggetto() != null && movimento.getSoggetto().getUid() != 0) {
			checkCondition(movimento.getSoggetto().getCodiceSoggetto() != null
					&& movimento.getSoggetto().getCodiceSoggetto().equals(model.getSoggetto().getCodiceSoggetto()),
					ErroreFin.SOGGETTO_DIVERSO_DA_QUELLO_DEL_MOVIMENTO.getErrore(tipo));
		} else if (movimento.getClasseSoggetto() != null) {
			checkClasseSoggetto(soggetto, movimento, proseguire);
		}
	}
	
	/**
	 * Controlla la classe del soggetto.
	 * 
	 * @param soggetto   il soggetto da controllare
	 * @param movimento  il movimento da controllare
	 * @param proseguire se proseguire con l'elaborazione a seguito della discrepanza tra classi
	 */
	private void checkClasseSoggetto(Soggetto soggetto, MovimentoGestione movimento, Boolean proseguire) {
		final String methodName = "checkClasseSoggetto";
		ClasseSoggetto classeSoggettoMovimentoGestione = movimento.getClasseSoggetto();
		if(classeSoggettoMovimentoGestione == null) {
			log.debug(methodName, "Classe soggetto non presente per il movimento di gestione");
			return;
		}
		List<ClassificazioneSoggetto> listaClasseSoggettoSoggetto = soggetto.getElencoClass();
		if(listaClasseSoggettoSoggetto == null) {
			log.debug(methodName, "Classi soggetto non presenti sul soggetto");
			return;
		}
		if(Boolean.TRUE.equals(proseguire)){
			log.debug(methodName, "Controllo classe impegno e soggetto da non effettuare: lo salto");
			return;
		}
		for(ClassificazioneSoggetto cs : listaClasseSoggettoSoggetto) {
			if(cs.getSoggettoClasseCode().equalsIgnoreCase(classeSoggettoMovimentoGestione.getCodice())) {
				return;
			}
		}
		addMessaggioForTipoMovimento();
	}

	/**
	 * Aggiunge il messaggio per il tipo movimento
	 */
	protected void addMessaggioForTipoMovimento() {
		addMessaggio(ErroreFin.PRESENZA_CLASSIFICAZIONE_SOGGETTO.getErrore());
	}
	
	/**
	 * Ricerca il provvisorio di cassa di spesa
	 * @return il provvisorio di cassa
	 */
	protected ProvvisorioDiCassa ricercaProvvisorioDiCassaSpesa() {
		model.getSubdocumentoSpesa().getProvvisorioCassa().setTipoProvvisorioDiCassa(TipoProvvisorioDiCassa.S);
		return ricercaProvvisorioDiCassa(model.getSubdocumentoSpesa().getProvvisorioCassa());
	}
	
	/**
	 * ricercaProvvisorioDiCassaEntrata
	 * @return il provvisorio trovato
	 */
	public ProvvisorioDiCassa ricercaProvvisorioDiCassaEntrata() {
		model.getSubdocumentoEntrata().getProvvisorioCassa().setTipoProvvisorioDiCassa(TipoProvvisorioDiCassa.E);

		return ricercaProvvisorioDiCassa(model.getSubdocumentoEntrata().getProvvisorioCassa());
	}

	
	/**
	 * Ricerca il provvisorio di cassa.
	 * 
	 * @return il provvisorio trovato
	 */
	private ProvvisorioDiCassa ricercaProvvisorioDiCassa(ProvvisorioDiCassa provvisorio) {
		
		RicercaProvvisorioDiCassaPerChiave reqRPK = model.creaRequestProvvisorioCassa(provvisorio);
		RicercaProvvisorioDiCassaPerChiaveResponse resRPK = provvisorioService.ricercaProvvisorioDiCassaPerChiave(reqRPK);
		
		return resRPK.getProvvisorioDiCassa();
	}
	
	/**
	 * Controlla la coerenza dei movimenti di gestione
	 * @param base       il movimento di riferimento
	 * @param subBase    il sub di riferimento
	 * @param toCheck    il movimento da controllare
	 * @param subToCheck il sub da controllare
	 * @return <code>true</code> se i movimenti sono coerenti; <code>false</code> altrimenti
	 */
	protected boolean isMovimentoCoerente(MovimentoGestione base, MovimentoGestione subBase, MovimentoGestione toCheck, MovimentoGestione subToCheck) {
		return base.getUid() == toCheck.getUid() && (
				(!numeroMovimentoPresente(subBase) && !numeroMovimentoPresente(subToCheck))
				|| (numeroMovimentoPresente(subBase) && numeroMovimentoPresente(subToCheck) && subBase.getNumero().equals(subToCheck.getNumero()))
			);
	}
	
	/**
	 * Controlla se il numero del movimento sia valorizzato
	 * @param movimentoGestione il movimento da controllare
	 * @return <code>true</code> se il numero &eacute; valorizzato; <code>false</code> altrimenti
	 */
	protected boolean numeroMovimentoPresente(MovimentoGestione movimentoGestione) {
		return movimentoGestione != null && movimentoGestione.getNumero() != null;
	}
	/**
	 * Controlla che l'utente possa associare il movimento gestione all'allegato atto
	 * @param movgest il movgest da controllare
	 * 
	 * @return <code>true</code> se l'utente  pu&ograve; associare il movimento gestione, <code>false</code> altrimenti.
	 */
	protected boolean isMovgestAssociabileDaUtente( MovimentoGestione movgest){
		return ( model.getAnnoEsercizioInt().equals(movgest.getAnnoMovimento())) || !AzioniConsentiteFactory.isConsentito(AzioniConsentite.ALLEGATO_ATTO_INSERISCI_NO_RESIDUI, sessionHandler.getAzioniConsentite() ); 
	}
}

