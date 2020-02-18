/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.helper;

import java.util.Date;
import java.util.EnumSet;

import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;


/**
 * Classe di utilit&agrave; per uniformare la creazione delle request per la ricerca per chiave dei movimenti di gestione.
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 24/11/2017
 *
 */
public final class MovimentoGestioneHelper {
		
	/** Non instanziare la classe */
	private MovimentoGestioneHelper() {
	}
	
	/**
	 * Permette di ottenere la request da utilizzare per il servizio {@link RicercaImpegnoPerChiaveOttimizzato} per ottenere un impegno senza caricarne i subimpegni.
	 * 
	 * @param annoEsercizio l'anno di esercizio
	 * @param ente          l'ente
	 * @param richiedente   il richiedente
	 * @param impegno       i dati dell'impegno da ricercare
	 * @return req          la request creata
	 */
	public static RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoPerChiaveOttimizzato(Integer annoEsercizio, Ente ente, Richiedente richiedente, Impegno impegno){
		RicercaImpegnoPerChiaveOttimizzato req = popolaCampiComuniRequestRicercaImpegnoPerChiaveOttimizzato(annoEsercizio, ente, richiedente, impegno);
		
		req.setCaricaSub(false);
		
		return req;
	}
	
	/**
	 * Permette di ottenere la request da utilizzare per il servizio {@link RicercaImpegnoPerChiaveOttimizzato} per ottenere un determinato subimpegno.
	 * 
	 * @param annoEsercizio l'anno di esercizio
	 * @param ente          l'ente
	 * @param richiedente   il richiedente
	 * @param impegno       i dati dell'impegno da ricercare
	 * @param subImpegno    i dati del subImpegno da ricercare
	 * @return req la request creata
	 */
	public static RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoPerChiaveOttimizzato(Integer annoEsercizio, Ente ente, Richiedente richiedente, Impegno impegno, SubImpegno subImpegno){
				
		if(subImpegno == null || subImpegno.getNumero() == null){
			return creaRequestRicercaImpegnoPerChiaveOttimizzato(annoEsercizio, ente, richiedente, impegno);
		}
		RicercaImpegnoPerChiaveOttimizzato req = popolaCampiComuniRequestRicercaImpegnoPerChiaveOttimizzato(annoEsercizio, ente, richiedente, impegno);
		req.setCaricaSub(true);
		req.getpRicercaImpegnoK().setNumeroSubDaCercare(subImpegno.getNumero());
		
		return req;
	}
	
	/**
	 * Permette di ottenere la request da utilizzare per il servizio {@link RicercaImpegnoPerChiaveOttimizzato} per ottenere un determinato subimpegno.
	 * 
	 * @param annoEsercizio        l'anno di esercizio
	 * @param ente                 l'ente
	 * @param richiedente          il richiedente
	 * @param impegno              i dati dell'impegno da ricercare
	 * @param parametriPaginazione i parametri di paginazione
	 * @return req la request creata
	 */
	public static RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoPerChiaveOttimizzato(Integer annoEsercizio, Ente ente, Richiedente richiedente, Impegno impegno, ParametriPaginazione parametriPaginazione){
		
		RicercaImpegnoPerChiaveOttimizzato req = popolaCampiComuniRequestRicercaImpegnoPerChiaveOttimizzato(annoEsercizio, ente, richiedente, impegno);
		req.setCaricaSub(true);
		req.setSubPaginati(true);		
		req.setFiltroSubSoloInQuestoStato(StatoOperativoMovimentoGestione.DEFINITIVO.getCodice());
		req.setNumPagina(parametriPaginazione.getNumeroPagina());
		req.setNumRisultatiPerPagina(parametriPaginazione.getNumeroPagina());
		
		return req;
	}
	
	
	/**
	 * Popola i campi comuni della request di ricercaImpegnoPerChiaveOttimizzato per il servizio di {@link RicercaImpegnoPerChiaveOttimizzato}
	 * 
	 * @param annoEsercizio l'anno di esercizio
	 * @param ente 			l'ente
	 * @param richiedente 	il richiedente
	 * @param impegno 		i dati dell'impegno da ricercare
	 * @return req 			la request creata
	 * */
	private static RicercaImpegnoPerChiaveOttimizzato popolaCampiComuniRequestRicercaImpegnoPerChiaveOttimizzato(Integer annoEsercizio, Ente ente, Richiedente richiedente, Impegno impegno){
		RicercaImpegnoPerChiaveOttimizzato req = new RicercaImpegnoPerChiaveOttimizzato();
		
		req.setDataOra(new Date());
		req.setRichiedente(richiedente);
		
		RicercaImpegnoK ricercaImpegnoK = new RicercaImpegnoK();
		ricercaImpegnoK.setAnnoEsercizio(annoEsercizio);
		ricercaImpegnoK.setAnnoImpegno(impegno.getAnnoMovimento());
		ricercaImpegnoK.setNumeroImpegno(impegno.getNumero());
		
		req.setpRicercaImpegnoK(ricercaImpegnoK);
		req.setEnte(ente);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		req.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		// Non richiedo NESSUN importo derivato.
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		// Non richiedo NESSUN classificatore
		datiOpzionaliCapitoli.setTipologieClassificatoriRichiesti(EnumSet.of(TipologiaClassificatore.CDC, TipologiaClassificatore.TIPO_FINANZIAMENTO, TipologiaClassificatore.MACROAGGREGATO));
		req.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		
		return req;
	}
	
	/**
	 * Permette di ottenere la request da utilizzare per il servizio {@link RicercaImpegnoPerChiaveOttimizzato} per ottenere un impegno senza caricarne i subimpegni.
	 * 
	 * @param annoEsercizio l'anno di esercizio
	 * @param ente          l'ente
	 * @param richiedente   il richiedente
	 * @param accertamento       i dati dell'impegno da ricercare
	 * @return req          la request creata
	 */
	public static RicercaAccertamentoPerChiaveOttimizzato creaRequestRicercaAccertamentoPerChiaveOttimizzato(Integer annoEsercizio, Ente ente, Richiedente richiedente, Accertamento accertamento){
		RicercaAccertamentoPerChiaveOttimizzato req = popolaCampiComuniRequestRicercaAccertamentoPerChiaveOttimizzato(annoEsercizio, ente, richiedente, accertamento);
		
		req.setCaricaSub(false);
		
		return req;
	}
	
	/**
	 * Permette di ottenere la request da utilizzare per il servizio {@link RicercaAccertamentoPerChiaveOttimizzato} per ottenere un determinato subaccertamento.
	 * 
	 * @param annoEsercizio l'anno di esercizio
	 * @param ente          l'ente
	 * @param richiedente   il richiedente
	 * @param accertamento       i dati dell'accertamento da ricercare
	 * @param subAccertamento    i dati del subAccertamento da ricercare
	 * @return req la request creata
	 */
	public static RicercaAccertamentoPerChiaveOttimizzato creaRequestRicercaAccertamentoPerChiaveOttimizzato(Integer annoEsercizio, Ente ente, Richiedente richiedente, Accertamento accertamento, SubAccertamento subAccertamento){
				
		if(subAccertamento == null){
			return creaRequestRicercaAccertamentoPerChiaveOttimizzato(annoEsercizio, ente, richiedente, accertamento);
		}
		RicercaAccertamentoPerChiaveOttimizzato req = popolaCampiComuniRequestRicercaAccertamentoPerChiaveOttimizzato(annoEsercizio, ente, richiedente, accertamento);
		req.setCaricaSub(true);
		req.getpRicercaAccertamentoK().setNumeroSubDaCercare(subAccertamento.getNumero());
		
		return req;
	}
	
	/**
	 * Popola i campi comuni della request di ricercaImpegnoPerChiaveOttimizzato per il servizio di {@link RicercaImpegnoPerChiaveOttimizzato}
	 * 
	 * @param annoEsercizio l'anno di esercizio
	 * @param ente 			l'ente
	 * @param richiedente 	il richiedente
	 * @param accertamento 		i dati dell'impegno da ricercare
	 * @return req 			la request creata
	 * */
	private static RicercaAccertamentoPerChiaveOttimizzato popolaCampiComuniRequestRicercaAccertamentoPerChiaveOttimizzato(Integer annoEsercizio, Ente ente, Richiedente richiedente, Accertamento accertamento){
		RicercaAccertamentoPerChiaveOttimizzato req = new RicercaAccertamentoPerChiaveOttimizzato();
		
		req.setDataOra(new Date());
		req.setRichiedente(richiedente);
		
		RicercaAccertamentoK ricercaAccertamentoK = new RicercaAccertamentoK();
		ricercaAccertamentoK.setAnnoEsercizio(annoEsercizio);
		ricercaAccertamentoK.setAnnoAccertamento(accertamento.getAnnoMovimento());
		ricercaAccertamentoK.setNumeroAccertamento(accertamento.getNumero());
		
		req.setpRicercaAccertamentoK(ricercaAccertamentoK);
		req.setEnte(ente);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		req.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		//NON SPOSTARE O SOVRASCRIVERE: il movgest puo' essere messo in sessione, i dati opzionali devono essere sempre caricati nello stesso modo
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		datiOpzionaliCapitoli.setTipologieClassificatoriRichiesti(EnumSet.of(TipologiaClassificatore.CDC, TipologiaClassificatore.TIPO_FINANZIAMENTO));
		req.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		
		return req;
	}
	
}
