/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.movimentogestione;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.BaseFactory;
import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siacfinser.model.MovimentoGestione;


public abstract class ElementoMovimentoGestioneFactory extends BaseFactory {

	protected static <EMG extends ElementoMovimentoGestione> 
		EMG getInstance(Class<EMG> movimentoGestioneClass, MovimentoGestione movimentoGestione) {
	
		EMG elementoMovimentoGestione = ReflectionUtil.silentlyBuildInstance(movimentoGestioneClass);
		
		elementoMovimentoGestione.setUid(movimentoGestione.getUid());
		elementoMovimentoGestione.setAnno(String.valueOf(movimentoGestione.getAnno()));
		elementoMovimentoGestione.setNumero(String.valueOf(movimentoGestione.getNumero()));
		
		return elementoMovimentoGestione;
	}
	
	protected static <EMG extends ElementoMovimentoGestione> 
		void fillCommonFields(EMG elementoMovimentoGestione, MovimentoGestione movimentoGestione) {
	
		fillDettaglioFields(elementoMovimentoGestione, movimentoGestione);

		elementoMovimentoGestione.setStato(movimentoGestione.getStatoOperativo().getDescrizione());
		elementoMovimentoGestione.setCapitolo(movimentoGestione.getCapitolo().getAnnoCapitoloArticolo());
		
		if(movimentoGestione.getCapitolo().getTipoFinanziamento() != null)
			elementoMovimentoGestione.setTipoFinanziamento(movimentoGestione.getCapitolo().getTipoFinanziamento().getDescrizione());
		
		if(movimentoGestione.getComponenteBilancioMovimentoGestione() != null)
			elementoMovimentoGestione.setComponenteBilancio(movimentoGestione.getComponenteBilancioMovimentoGestione().getDescrizioneTipoComponente());
	
		
		elementoMovimentoGestione.setProvvedimento(movimentoGestione.getAttoAmministrativo().getDescrizioneCompleta());
		
		if(movimentoGestione.getAttoAmministrativo().getTipoAtto() != null)
			elementoMovimentoGestione.setTipoAtto(movimentoGestione.getAttoAmministrativo().getTipoAtto().getCodiceDescrizione());
		
		if(movimentoGestione.getAttoAmministrativo().getStrutturaAmmContabile() != null)
			elementoMovimentoGestione.setStrutturaAmministrativa(movimentoGestione.getAttoAmministrativo().getStrutturaAmmContabile().getCodiceDescrizione());
		
		if(movimentoGestione.getSoggetto() != null)
			elementoMovimentoGestione.setSoggetto(movimentoGestione.getSoggetto().getCodiceDenominazione());	
	}
	
	protected static <EMG extends ElementoMovimentoGestione> 
		void fillDettaglioFields(EMG elementoMovimentoGestione, MovimentoGestione movimentoGestione) {
		
		elementoMovimentoGestione.setAnnoBilancio(String.valueOf(movimentoGestione.getBilancio().getAnno()));
		elementoMovimentoGestione.setImportoAttuale(NumberUtil.toImporto(movimentoGestione.getImportoAttuale()));
		elementoMovimentoGestione.setImportoIniziale(NumberUtil.toImporto(movimentoGestione.getImportoIniziale()));
	}


}
