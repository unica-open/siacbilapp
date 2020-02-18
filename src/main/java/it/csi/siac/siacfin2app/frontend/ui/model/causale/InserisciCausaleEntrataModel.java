/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.causale;

import java.util.Date;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceCausaleEntrata;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoCausale;

/**
 * Model per l'inserimento della causale di entrata.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 12/05/2014
 *
 */
public class InserisciCausaleEntrataModel extends GenericCausaleEntrataModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1501485679166540181L;

	/** Costruttore vuoto di default */
	public InserisciCausaleEntrataModel() {
		super();
		setTitolo("Inserimento causale incasso");
	}

	/* Request */
	
	@Override
	public InserisceCausaleEntrata creaRequestInserimentoCausaleEntrata() {
		InserisceCausaleEntrata request = new InserisceCausaleEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		CausaleEntrata causale = getCausale();
		
		causale.setEnte(getEnte());
		
		causale.setStatoOperativoCausale(StatoOperativoCausale.VALIDA);
		causale.setTipoCausale(getTipoCausale());
		causale.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));
		causale.setCapitoloEntrataGestione(impostaEntitaFacoltativa(getCapitolo()));
		causale.setAccertamento(impostaEntitaFacoltativa(getMovimentoGestione()));
		causale.setSubAccertamento(impostaEntitaFacoltativa(getSubMovimentoGestione()));
		causale.setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		causale.setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
		
		request.setCausaleEntrata(causale);
		
		return request;
	}
	
	/* Utility */
	
	@Override
	public void impostaCausale(CausaleEntrata causaleEntrata) {
		setCausale(causaleEntrata);
		setCapitolo(causaleEntrata.getCapitoloEntrataGestione());
		setSoggetto(causaleEntrata.getSoggetto());
		setMovimentoGestione(causaleEntrata.getAccertamento());
		setSubMovimentoGestione(causaleEntrata.getSubAccertamento());
	
		setStrutturaAmministrativoContabile(causaleEntrata.getStrutturaAmministrativoContabile());
		setTipoCausale(causaleEntrata.getTipoCausale());
		
		impostaAttoAmministrativo(causaleEntrata.getAttoAmministrativo());
	}
	
}
