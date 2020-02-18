/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.util.wrappers.registroa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccecser.model.EventoRegistroACespiteSelector;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.OperazioneTipoImporto;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Factory per Elemento delle scritture per lo step 1 della PrimaNotaIntegrata
 * @author elisa
 * @version 1.0.0 - 02-11-2018
 *
 */
public final class ElementoMovimentoEPRegistroAFactory {
	
	/** Non instanziare la classe */
	private ElementoMovimentoEPRegistroAFactory() {
	}
	
	/**
	 * Gets the single instance of ElementoMovimentoEPRegistroAFactory.
	 *
	 * @param movimentoEP the movimento EP
	 * @return single instance of ElementoMovimentoEPRegistroAFactory
	 */
	public static final ElementoMovimentoEPRegistroA getInstance(MovimentoEP movimentoEP) {
		if(movimentoEP == null) {
//			throw new FrontEndBusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("impossibile istanziare un elemento movimento ep, il movimento risulta essere null").getTesto());
		}
		DatiContoCespite datiContoCespite = new DatiContoCespite();
		List<Conto> contiImposta = ottieniListaContiImposta(movimentoEP.getCausaleEP());
		Evento eventoCespite = getEventoMovimentoEP(movimentoEP);
		impostaDatiDaMovimentoDettaglio(datiContoCespite, movimentoEP.getListaMovimentoDettaglio(), contiImposta);
		return new ElementoMovimentoEPRegistroA(movimentoEP, datiContoCespite.importoContoCespiteMovimento,
				datiContoCespite.calcolaImportoTotaleCespitiCollegati(), datiContoCespite.contoCespite, eventoCespite, datiContoCespite.hasContoCespite, datiContoCespite.hasContoImposta,
				datiContoCespite.listaCespitiCollegati);
	}

	/**
	 * Gets the evento movimento EP.
	 *
	 * @param movimentoEP the movimento EP
	 * @return the evento movimento EP
	 */
	private static final Evento getEventoMovimentoEP(MovimentoEP movimentoEP) {
		RegistrazioneMovFin registrazioneMovFin = movimentoEP.getRegistrazioneMovFin();
		if(registrazioneMovFin != null && registrazioneMovFin.getEvento() != null) {
			return registrazioneMovFin.getEvento();
		}
		CausaleEP causaleEP = movimentoEP.getCausaleEP();
		if(causaleEP == null || causaleEP.getEventi() == null || causaleEP.getEventi().isEmpty()) {
			return null;
		}
		for (Evento ev : causaleEP.getEventi()) {
			if(EventoRegistroACespiteSelector.isCodiceEventoRegistroA(ev.getCodice(), causaleEP.getTipoCausale())) {
				return ev;
			}
		}	
		
		return null;
	}
	

	/**
	 * Imposta dati da movimento dettaglio.
	 * @param datiContoCespite 
	 *
	 * @param instance the instance
	 * @param listaMovimentoDettaglio the lista movimento dettaglio
	 * @param contiImposta the conti imposta
	 */
	private static void impostaDatiDaMovimentoDettaglio(DatiContoCespite datiContoCespite, List<MovimentoDettaglio> listaMovimentoDettaglio, List<Conto> contiImposta) {
		boolean contoImposta = false;
		for (MovimentoDettaglio movimentoDettaglio : listaMovimentoDettaglio) {
			Conto conto = movimentoDettaglio.getConto();
			if( conto == null || conto.getTipoConto() == null) {
				continue;
			}
			impostaDatiContoCespite(datiContoCespite, movimentoDettaglio, conto);
			
			contoImposta = contoImposta || (contiImposta != null && ComparatorUtils.searchByUidEventuallyNull(contiImposta, conto) != null);
			datiContoCespite.hasContoImposta = contoImposta;
		}
	}

	/**
	 * Imposta dati da causale EP.
	 *
	 * @param causaleEP the causale EP
	 * @return the list
	 */
	private static List<Conto> ottieniListaContiImposta(CausaleEP causaleEP) {
		List<Conto> contos = new ArrayList<Conto>();
		if(causaleEP == null || causaleEP.getContiTipoOperazione()== null || causaleEP.getContiTipoOperazione().isEmpty()) {
			return contos;
		}
		for (ContoTipoOperazione cto : causaleEP.getContiTipoOperazione()) {
			if(OperazioneTipoImporto.IMPOSTA.equals(cto.getOperazioneTipoImporto()) && cto.getConto() != null) {
				contos.add(cto.getConto());
			}
		}
		return contos;
	}

	/**
	 * Imposta dati conto cespite.
	 *
	 * @param instance the instance
	 * @param movimentoDettaglio the movimento dettaglio
	 * @param conto the conto
	 */
	private static void impostaDatiContoCespite(DatiContoCespite datiContoCespite, MovimentoDettaglio movimentoDettaglio, Conto conto) {
		if(!conto.getTipoConto().isTipoCespiti()) {
			return;
		}
		datiContoCespite.contoCespite = conto;
		datiContoCespite.hasContoCespite = Boolean.TRUE;
		//IMPRTANTE: mi baso sul fatto che i cespiti siano sempre collegati al movimento dettaglio con il conto di tipo cespite!
		datiContoCespite.addCespitiCollegati(movimentoDettaglio.getCespiti());
		datiContoCespite.addImportoContoCespiteMovimento(movimentoDettaglio.getImporto());
	}
	
	private static final class DatiContoCespite {
		//e' uno dei pochi casi in cui ha senso che siano package proceted
		BigDecimal importoContoCespiteMovimento = BigDecimal.ZERO;
		Conto contoCespite;
		Boolean hasContoCespite;
		Boolean hasContoImposta;
		List<Cespite> listaCespitiCollegati = new ArrayList<Cespite>();
		/** Constructor */
		DatiContoCespite() {
			// Package-protected constructor to syntetic accessor
		}
		/**
		 * Adds the importo conto cespite movimento.
		 *
		 * @param augend the augend
		 */
		void addImportoContoCespiteMovimento(BigDecimal augend) {
			if(augend != null) {
				importoContoCespiteMovimento = importoContoCespiteMovimento.add(augend);
			}
		}
		
		public void addCespitiCollegati(List<Cespite> cespiti) {
			if(cespiti != null && !cespiti.isEmpty()) {
				listaCespitiCollegati.addAll(cespiti);
			}
		}

		/**
		 * Calcola importo totale cespiti collegati.
		 *
		 * @return the big decimal
		 */
		BigDecimal calcolaImportoTotaleCespitiCollegati() {
			BigDecimal importoTotaleCespitiCollegati = BigDecimal.ZERO;
			for (Cespite cespite : listaCespitiCollegati) {
				importoTotaleCespitiCollegati = importoTotaleCespitiCollegati.add(cespite.getImportoSuRegistroA());
			}
			return importoTotaleCespitiCollegati;
		}
	}
	
}
