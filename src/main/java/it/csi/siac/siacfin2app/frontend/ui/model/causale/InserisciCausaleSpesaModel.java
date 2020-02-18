/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.causale;

import java.util.Date;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceCausaleSpesa;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoCausale;

/**
 * Model per l'inserimento della causale di spesa.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 12/05/2014
 *
 */
public class InserisciCausaleSpesaModel extends GenericCausaleSpesaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 995764901267041775L;

	/** Costruttore vuoto di default */
	public InserisciCausaleSpesaModel() {
		super();
		setTitolo("Inserimento causale pagamento");
	}

	/* Request */
	
	/**
	  * Crea una request per il servizio di Inserimento Causale Spesa
	  * 
	  * @return la request creata
	  */
	public InserisceCausaleSpesa creaRequestInserimentoCausaleSpesa() {
		InserisceCausaleSpesa request = new InserisceCausaleSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		CausaleSpesa causale = getCausale();
		
		causale.setEnte(getEnte());
		
		causale.setStatoOperativoCausale(StatoOperativoCausale.VALIDA);
		causale.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));
		causale.setCapitoloUscitaGestione(impostaEntitaFacoltativa(getCapitolo()));
		causale.setImpegno(impostaEntitaFacoltativa(getMovimentoGestione()));
		causale.setSubImpegno(impostaEntitaFacoltativa(getSubMovimentoGestione()));
		causale.setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		causale.setSedeSecondariaSoggetto(impostaEntitaFacoltativa(getSedeSecondariaSoggetto()));
		causale.setModalitaPagamentoSoggetto(impostaEntitaFacoltativa(getModalitaPagamentoSoggetto()));
		causale.setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
		
		if(causale.getImpegno() != null && causale.getCapitoloUscitaGestione() == null){
			causale.setCapitoloUscitaGestione(impostaEntitaFacoltativa(causale.getImpegno().getCapitoloUscitaGestione()));
		}
		
		request.setCausaleSpesa(causale);
		
		return request;
	}
	
	/* Utility */
	
	@Override
	public void impostaCausale(CausaleSpesa causaleSpesa) {
		setCausale(causaleSpesa);
		setCapitolo(causaleSpesa.getCapitoloUscitaGestione());
		setSoggetto(causaleSpesa.getSoggetto());
		setMovimentoGestione(causaleSpesa.getImpegno());
		setSubMovimentoGestione(causaleSpesa.getSubImpegno());
	
		setSedeSecondariaSoggetto(causaleSpesa.getSedeSecondariaSoggetto());
		setModalitaPagamentoSoggetto(causaleSpesa.getModalitaPagamentoSoggetto());
		setStrutturaAmministrativoContabile(causaleSpesa.getStrutturaAmministrativoContabile());
		setTipoCausale(causaleSpesa.getTipoCausale());
		
		impostaAttoAmministrativo(causaleSpesa.getAttoAmministrativo());
	}
	
}
