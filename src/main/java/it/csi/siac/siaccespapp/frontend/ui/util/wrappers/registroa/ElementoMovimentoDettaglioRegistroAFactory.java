/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.util.wrappers.registroa;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccecser.model.EventoRegistroACespiteSelector;
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
public final class ElementoMovimentoDettaglioRegistroAFactory {
	
	/** Non instanziare la classe */
	private ElementoMovimentoDettaglioRegistroAFactory() {
	}
	
	/**
	 * Gets the single instance of ElementoMovimentoDettaglioRegistroAFactory.
	 *
	 * @param movimentoDettaglio the movimento dettaglio
	 * @return single instance of ElementoMovimentoDettaglioRegistroAFactory
	 */
	public static final ElementoMovimentoDettaglioRegistroA getInstance(MovimentoDettaglio movimentoDettaglio) {
		MovimentoEP movimentoEP = movimentoDettaglio.getMovimentoEP();
		Conto conto = movimentoDettaglio.getConto();
		List<Conto> contiImposta = ottieniListaContiImposta(movimentoEP.getCausaleEP());
		Evento eventoCespite = getEventoMovimentoEP(movimentoEP);
		boolean hasContoImposta = contiImposta != null && ComparatorUtils.searchByUidEventuallyNull(contiImposta, conto) != null;
		boolean hasContoCespite = conto.getTipoConto().isTipoCespiti();
		
		return new ElementoMovimentoDettaglioRegistroA(movimentoDettaglio, movimentoEP, conto, eventoCespite, Boolean.valueOf(hasContoCespite), Boolean.valueOf(hasContoImposta));
		
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
	 *
	 * @param datiContoCespite the dati conto cespite
	 * @param movimentoDettaglio the movimento dettaglio
	 * @param contiImposta the conti imposta
	 */
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
	
}
