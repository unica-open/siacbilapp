/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.variazione;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.result.CustomJSONResult;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.VariazioneDiBilancioService;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siaccorser.model.ServiceResponse;

/**
 * Classe astratta per la gestione dell'aggiornamento della variazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 09/01/2014
 * 
 * @param <M> la tipizzazione del model
 * 
 */
public abstract class VariazioneBaseAction<M extends GenericBilancioModel> extends GenericBilancioAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -5947421201625760786L;

	/** Serviz&icirc; del capitolo di uscita previsione */
	@Autowired protected transient CapitoloUscitaPrevisioneService capitoloUscitaPrevisioneService;
	/** Serviz&icirc; del capitolo di uscita gestione */
	@Autowired protected transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	/** Serviz&icirc; del capitolo di entrata previsione */
	@Autowired protected transient CapitoloEntrataPrevisioneService capitoloEntrataPrevisioneService;
	/** Serviz&icirc; del capitolo di entrata gestione */
	@Autowired protected transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	/** Serviz&icirc; del provvedimento */
	@Autowired protected transient ProvvedimentoService provvedimentoService;
	/** Serviz&icirc; della variazione di bilancio */
	@Autowired protected transient VariazioneDiBilancioService variazioneDiBilancioService;
	/** Serviz&icirc; del capitolo */
	@Autowired protected transient CapitoloService capitoloService;

	/**
	 * Elimina il capitolo di uscita previsione.
	 * @param <RES> la tipizzazione della response
	 * 
	 * @param capitoloDaEliminare il capitolo da eliminare
	 * @return la response del servizio di eliminazione
	 */
	@SuppressWarnings("unchecked")
	protected <RES extends ServiceResponse> RES eliminaCapitoloUscitaPrevisione(Capitolo<?, ?> capitoloDaEliminare) {
		EliminaCapitoloUscitaPrevisione requestUscitaPrevisione = new EliminaCapitoloUscitaPrevisione();
		requestUscitaPrevisione.setBilancio(model.getBilancio());
		requestUscitaPrevisione.setCapitoloUscitaPrev((CapitoloUscitaPrevisione) capitoloDaEliminare);
		requestUscitaPrevisione.setDataOra(new Date());
		requestUscitaPrevisione.setEnte(model.getEnte());
		requestUscitaPrevisione.setRichiedente(model.getRichiedente());

		return (RES) capitoloUscitaPrevisioneService.eliminaCapitoloUscitaPrevisione(requestUscitaPrevisione);
	}

	/**
	 * Elimina il capitolo di uscita gestione.
	 * @param <RES> la tipizzazione della response
	 * @param capitoloDaEliminare il capitolo da eliminare
	 * @return la response del servizio di eliminazione
	 */
	@SuppressWarnings("unchecked")
	protected <RES extends ServiceResponse> RES eliminaCapitoloUscitaGestione(Capitolo<?, ?> capitoloDaEliminare) {
		EliminaCapitoloUscitaGestione requestUscitaGestione = new EliminaCapitoloUscitaGestione();
		requestUscitaGestione.setBilancio(model.getBilancio());
		requestUscitaGestione.setCapitoloUscitaGest((CapitoloUscitaGestione) capitoloDaEliminare);
		requestUscitaGestione.setDataOra(new Date());
		requestUscitaGestione.setEnte(model.getEnte());
		requestUscitaGestione.setRichiedente(model.getRichiedente());

		return (RES) capitoloUscitaGestioneService.eliminaCapitoloUscitaGestione(requestUscitaGestione);
	}

	/**
	 * Elimina il capitolo di entrata previsione.
	 * @param <RES> la tipizzazione della response
	 * @param capitoloDaEliminare il capitolo da eliminare
	 * @return la response del servizio di eliminazione
	 */
	@SuppressWarnings("unchecked")
	protected <RES extends ServiceResponse> RES eliminaCapitoloEntrataPrevisione(Capitolo<?, ?> capitoloDaEliminare) {
		EliminaCapitoloEntrataPrevisione requestEntrataPrevisione = new EliminaCapitoloEntrataPrevisione();
		requestEntrataPrevisione.setBilancio(model.getBilancio());
		requestEntrataPrevisione.setCapitoloEntrataPrev((CapitoloEntrataPrevisione) capitoloDaEliminare);
		requestEntrataPrevisione.setDataOra(new Date());
		requestEntrataPrevisione.setEnte(model.getEnte());
		requestEntrataPrevisione.setRichiedente(model.getRichiedente());

		return (RES) capitoloEntrataPrevisioneService.eliminaCapitoloEntrataPrevisione(requestEntrataPrevisione);
	}

	/**
	 * Elimina il capitolo di entrata gestione.
	 * @param <RES> la tipizzazione della response
	 * @param capitoloDaEliminare il capitolo da eliminare
	 * @return la response del servizio di eliminazione
	 */
	@SuppressWarnings("unchecked")
	protected <RES extends ServiceResponse> RES eliminaCapitoloEntrataGestione(Capitolo<?, ?> capitoloDaEliminare) {
		EliminaCapitoloEntrataGestione requestEntrataGestione = new EliminaCapitoloEntrataGestione();
		requestEntrataGestione.setBilancio(model.getBilancio());
		requestEntrataGestione.setCapitoloEntrataGest((CapitoloEntrataGestione) capitoloDaEliminare);
		requestEntrataGestione.setDataOra(new Date());
		requestEntrataGestione.setEnte(model.getEnte());
		requestEntrataGestione.setRichiedente(model.getRichiedente());

		return (RES) capitoloEntrataGestioneService.eliminaCapitoloEntrataGestione(requestEntrataGestione);
	}

	/**
	 * Verifica se il capitolo di uscita previsione sia annullabile.
	 * @param <RES> la tipizzazione della response
	 * @param capitoloDaAnnullare il capitolo da controllare
	 * @return la response del servizio di controllo
	 */
	@SuppressWarnings("unchecked")
	protected <RES extends ServiceResponse> RES verificaAnnullabilitaCapitoloUscitaPrevisione(Capitolo<?, ?> capitoloDaAnnullare) {
		VerificaAnnullabilitaCapitoloUscitaPrevisione requestUscitaPrevisione = new VerificaAnnullabilitaCapitoloUscitaPrevisione();
		requestUscitaPrevisione.setBilancio(model.getBilancio());
		requestUscitaPrevisione.setCapitolo((CapitoloUscitaPrevisione) capitoloDaAnnullare);
		requestUscitaPrevisione.setDataOra(new Date());
		requestUscitaPrevisione.setEnte(model.getEnte());
		requestUscitaPrevisione.setRichiedente(model.getRichiedente());
		

		return (RES) capitoloUscitaPrevisioneService.verificaAnnullabilitaCapitoloUscitaPrevisione(requestUscitaPrevisione);
	}

	/**
	 * Verifica se il capitolo di uscita gestione sia annullabile.
	 * @param <RES> la tipizzazione della response
	 * @param capitoloDaAnnullare il capitolo da controllare
	 * @return la response del servizio di controllo
	 */
	@SuppressWarnings("unchecked")
	protected <RES extends ServiceResponse> RES verificaAnnullabilitaCapitoloUscitaGestione(Capitolo<?, ?> capitoloDaAnnullare) {
		VerificaAnnullabilitaCapitoloUscitaGestione requestUscitaGestione = new VerificaAnnullabilitaCapitoloUscitaGestione();
		requestUscitaGestione.setBilancio(model.getBilancio());
		requestUscitaGestione.setCapitolo((CapitoloUscitaGestione) capitoloDaAnnullare);
		requestUscitaGestione.setDataOra(new Date());
		requestUscitaGestione.setEnte(model.getEnte());
		requestUscitaGestione.setRichiedente(model.getRichiedente());

		

		return (RES) capitoloUscitaGestioneService.verificaAnnullabilitaCapitoloUscitaGestione(requestUscitaGestione);
	}

	/**
	 * Verifica se il capitolo di entrata previsione sia annullabile.
	 * @param <RES> la tipizzazione della response
	 * @param capitoloDaAnnullare il capitolo da controllare
	 * @return la response del servizio di controllo
	 */
	@SuppressWarnings("unchecked")
	protected <RES extends ServiceResponse> RES verificaAnnullabilitaCapitoloEntrataPrevisione(Capitolo<?, ?> capitoloDaAnnullare) {
		VerificaAnnullabilitaCapitoloEntrataPrevisione requestEntrataPrevisione = new VerificaAnnullabilitaCapitoloEntrataPrevisione();
		requestEntrataPrevisione.setBilancio(model.getBilancio());
		requestEntrataPrevisione.setCapitolo((CapitoloEntrataPrevisione) capitoloDaAnnullare);
		requestEntrataPrevisione.setDataOra(new Date());
		requestEntrataPrevisione.setEnte(model.getEnte());
		requestEntrataPrevisione.setRichiedente(model.getRichiedente());

		return (RES) capitoloEntrataPrevisioneService.verificaAnnullabilitaCapitoloEntrataPrevisione(requestEntrataPrevisione);
	}

	/**
	 * Verifica se il capitolo di entrata gestione sia annullabile.
	 * @param <RES> la tipizzazione della response
	 * @param capitoloDaAnnullare il capitolo da controllare
	 * @return la response del servizio di controllo
	 */
	@SuppressWarnings("unchecked")
	protected <RES extends ServiceResponse> RES verificaAnnullabilitaCapitoloEntrataGestione(Capitolo<?, ?> capitoloDaAnnullare) {
		VerificaAnnullabilitaCapitoloEntrataGestione requestEntrataGestione = new VerificaAnnullabilitaCapitoloEntrataGestione();
		requestEntrataGestione.setBilancio(model.getBilancio());
		requestEntrataGestione.setCapitolo((CapitoloEntrataGestione) capitoloDaAnnullare);
		requestEntrataGestione.setDataOra(new Date());
		requestEntrataGestione.setEnte(model.getEnte());
		requestEntrataGestione.setRichiedente(model.getRichiedente());


		return (RES) capitoloEntrataGestioneService.verificaAnnullabilitaCapitoloEntrataGestione(requestEntrataGestione);
	}
	
	/**
	 * Risultato per il controllo se il risultato async sia presente.
	 * 
	 * @author Marchino Alessandro
	 * @version 1.0.0 - 07/07/2016
	 *
	 */
	public static class AsyncResponsePresentVariazioneImportiResult extends CustomJSONResult {
		/** Per la serializzazione */
		private static final long serialVersionUID = 3541102918432843926L;
		/** Propriet&agrave; da includere nel JSON creato */
		private static final String INCLUDE_PROPERTIES = "errori.*, messaggi.*, informazioni.*, isAsyncResponsePresent";

		/** Empty default constructor */
		public AsyncResponsePresentVariazioneImportiResult() {
			super();
			setIncludeProperties(INCLUDE_PROPERTIES);
		}
	}
	
	/**
	 * Risultato per il controllo se il risultato async sia presente, con la richiesta della quadratura importi.
	 * 
	 * @author Marchino Alessandro
	 * @version 1.0.0 - 07/07/2016
	 *
	 */
	public static class AsyncResponsePresentConRichiestaConfermaVariazioneImportiResult extends CustomJSONResult {
		/** Per la serializzazione */
		private static final long serialVersionUID = 3541102918432843926L;
		/** Propriet&agrave; da includere nel JSON creato */
		private static final String INCLUDE_PROPERTIES = "errori.*, messaggi.*, informazioni.*, richiediConfermaQuadratura, richiediConfermaMancanzaProvvedimentoVariazioneBilancio, isAsyncResponsePresent";

		/** Empty default constructor */
		public AsyncResponsePresentConRichiestaConfermaVariazioneImportiResult() {
			super();
			setIncludeProperties(INCLUDE_PROPERTIES);
		}
	}
	
	/**
	 * Risultato per l'ottenimento dei dati della specifica degli importi.
	 * 
	 * @author Marchino Alessandro
	 * @version 1.0.0 - 07/07/2016
	 *
	 */
	public static class SpecificaVariazioneImportiResult extends CustomJSONResult {
		/** Per la serializzazione */
		private static final long serialVersionUID = 3541102918432843926L;
		/** Propriet&agrave; da includere nel JSON creato */
		private static final String INCLUDE_PROPERTIES = "errori.*, messaggi.*, informazioni.*, specificaImporti\\.cassaIncongruente, specificaImporti\\.cassaIncongruenteDopoDefinizione";

		/** Empty default constructor */
		public SpecificaVariazioneImportiResult() {
			super();
			setIncludeProperties(INCLUDE_PROPERTIES);
		}
	}
	
	/**
	 * Risultato per l'ottenimento dei dati della specifica degli importi.
	 * 
	 * @author Elisa Chiari
	 * @version 1.0.0 - 03/01/2017
	 *
	 */
	public static class SpecificaVariazioneImportiUEBResult extends CustomJSONResult {
		//Aggiunto per comodita' e coerenza
		/** Per la serializzazione*/
		private static final long serialVersionUID = -6625955488960758536L;
		/** Propriet&agrave; da includere nel JSON creato */
		private static final String INCLUDE_PROPERTIES = "errori.*, messaggi.*, informazioni.*, specificaUEB\\.cassaIncongruente, specificaUEB\\.cassaIncongruenteDopoDefinizione";

		/** Empty default constructor */
		public SpecificaVariazioneImportiUEBResult() {
			super();
			setIncludeProperties(INCLUDE_PROPERTIES);
		}
	}
}
