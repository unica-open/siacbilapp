/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.mutuo;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.BaseFactory;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;
import it.csi.siac.siaccommon.util.number.NumberUtil;


public final class ElementoMutuoFactory extends BaseFactory {

	private ElementoMutuoFactory() {
	}


	public static ElementoMutuo getInstance(Mutuo mutuo) {
		ElementoMutuo result = new ElementoMutuo();

		int uid = mutuo.getUid();

		String numero = mutuo.getNumero().toString();
		String tipoTasso = mutuo.getTipoTasso().getDescrizione();
		String tassoInteresse = NumberUtil.toDecimal(mutuo.getTassoInteresse());
		String euribor = NumberUtil.toDecimal(mutuo.getTassoInteresseEuribor());
		String spread = NumberUtil.toDecimal(mutuo.getTassoInteresseSpread());
		
		String provvedimento = mutuo.getAttoAmministrativo() != null ?  
				String.format("%d/%d/%s", mutuo.getAttoAmministrativo().getAnno(), 
										  mutuo.getAttoAmministrativo().getNumero(), 
										  capitaliseString(mutuo.getAttoAmministrativo().getTipoAtto().getDescrizione())) 
				: "";

		String tipo = mutuo.getAttoAmministrativo() != null ? 
				String.format("%s - %s", mutuo.getAttoAmministrativo().getTipoAtto().getCodice(), 
									     capitaliseString(mutuo.getAttoAmministrativo().getTipoAtto().getDescrizione())) 
				: "";  

		String strutturaAmministrativa = mutuo.getAttoAmministrativo() != null && mutuo.getAttoAmministrativo().getStrutturaAmmContabile() != null ? 
				String.format("%s - %s", mutuo.getAttoAmministrativo().getStrutturaAmmContabile().getCodice(), 
										 capitaliseString(mutuo.getAttoAmministrativo().getStrutturaAmmContabile().getDescrizione())) 
				: "";

		String soggetto = mutuo.getSoggetto() != null ? 
				String.format("%s - %s", mutuo.getSoggetto().getCodiceSoggetto(), capitaliseString(mutuo.getSoggetto().getDenominazione())) : "";

		String sommaMutuataIniziale = NumberUtil.toImporto(mutuo.getSommaMutuataIniziale());

		String periodoRimborso = mutuo.getPeriodoRimborso() != null ? 
				String.format("%s - %s", mutuo.getPeriodoRimborso().getCodice(), capitaliseString(mutuo.getPeriodoRimborso().getDescrizione())) : "";

		result.setUid(uid);
		result.setNumero(numero);
		result.setTipoTasso(tipoTasso);
		result.setCodiceStatoMutuo(mutuo.getStatoMutuo().getCodice());
		result.setDescrizioneStatoMutuo(mutuo.getStatoMutuo().getDescrizione());
		result.setPeriodoRimborso(periodoRimborso);
		result.setTassoInteresse(tassoInteresse);
		result.setEuribor(euribor);
		result.setSpread(spread);
		result.setTassoInteresse(tassoInteresse);
		result.setProvvedimento(provvedimento);
		result.setTipo(tipo);
		result.setStrutturaAmministrativa(strutturaAmministrativa);
		result.setSoggetto(soggetto);
		result.setSommaMutuataIniziale(sommaMutuataIniziale);
		
		return result;
	}


	public static List<ElementoMutuo> getInstances(List<Mutuo> mutui) {
		List<ElementoMutuo> result = new ArrayList<ElementoMutuo>();

		for (Mutuo mutuo : mutui) {
			result.add(getInstance(mutuo));
		}
		return result;
	}

}
